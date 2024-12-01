using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Security.Cryptography.X509Certificates;
using System.Net.Security;

namespace Lab_4;

public class FileDownloader
{
    public static void DownloadFile(string fileUrl, string localPath)
    {
        // Parse the URL into components (e.g., host, path)
        Uri uri = new Uri(fileUrl);
        string host = uri.Host;
        string path = uri.AbsolutePath;
        int port = 443;  // HTTPS port

        // Create a TCP connection to the server
        using (TcpClient client = new TcpClient(host, port))
        {
            // Establish SSL/TLS connection
            SslStream sslStream = new SslStream(client.GetStream(), false,
                new RemoteCertificateValidationCallback((sender, certificate, chain, sslPolicyErrors) => true));

            sslStream.AuthenticateAsClient(host);

            // Prepare the HTTP GET request to download the file
            string request = $"GET {path} HTTP/1.1\r\n" +
                             $"Host: {host}\r\n" +
                             "Connection: close\r\n\r\n";
            byte[] requestBytes = Encoding.ASCII.GetBytes(request);

            sslStream.Write(requestBytes, 0, requestBytes.Length);

            // Read the response (headers + body)
            using (MemoryStream responseStream = new MemoryStream())
            {
                byte[] buffer = new byte[1024];
                int bytesRead;
                bool headerEnded = false;

                while ((bytesRead = sslStream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    responseStream.Write(buffer, 0, bytesRead);
                    string responseText = Encoding.ASCII.GetString(responseStream.ToArray());

                    // Check if we've passed the headers and reached the body (file data)
                    if (!headerEnded && responseText.Contains("\r\n\r\n"))
                    {
                        headerEnded = true;
                        // Trim the headers part, keeping only the body
                        int index = responseText.IndexOf("\r\n\r\n") + 4;
                        responseStream.Seek(index, SeekOrigin.Begin);
                    }
                }

                // Write the file to the local path
                File.WriteAllBytes(localPath, responseStream.ToArray());
            }
        }

        Console.WriteLine("File downloaded successfully.");
    }
}
