using System.Net.Security;
using System.Net.Sockets;
using System.Text;

namespace Lab_4;

public class FileDownloader
{
    public static async Task DownloadFileAsync(string fileUrl, string localPath)
    {
        // Parse the URL into components (e.g., host, path)
        var uri = new Uri(fileUrl);
        var host = uri.Host;
        var path = uri.AbsolutePath;
        var port = 443; // HTTPS port

        // Create a TCP connection to the server
        using (var client = new TcpClient())
        {
            await client.ConnectAsync(host, port);

            // Establish SSL/TLS connection
            using (var sslStream = new SslStream(client.GetStream(), false))
            {
                await sslStream.AuthenticateAsClientAsync(host);

                // Prepare the HTTP GET request to download the file
                var request = $"GET {path} HTTP/1.1\r\n" +
                              $"Host: {host}\r\n" +
                              "Connection: close\r\n\r\n";
                var requestBytes = Encoding.ASCII.GetBytes(request);

                await sslStream.WriteAsync(requestBytes, 0, requestBytes.Length);

                // Read the response (headers + body)
                using (var responseStream = new MemoryStream())
                {
                    var buffer = new byte[1024];
                    int bytesRead;
                    var headerEnded = false;

                    while ((bytesRead = await sslStream.ReadAsync(buffer, 0, buffer.Length)) > 0)
                    {
                        responseStream.Write(buffer, 0, bytesRead);
                        var responseText = Encoding.ASCII.GetString(responseStream.ToArray());

                        // Check if we've passed the headers and reached the body (file data)
                        if (!headerEnded && responseText.Contains("\r\n\r\n"))
                        {
                            headerEnded = true;
                            // Trim the headers part, keeping only the body
                            var index = responseText.IndexOf("\r\n\r\n") + 4;
                            responseStream.Seek(index, SeekOrigin.Begin);
                        }
                    }

                    // Write the file to the local path
                    await File.WriteAllBytesAsync(localPath, responseStream.ToArray());
                }
            }
        }

        Console.WriteLine($"File downloaded successfully to {localPath}.");
    }

    public static void DownloadFile(string fileUrl, string localPath)
    {
        // Parse the URL into components (e.g., host, path)
        var uri = new Uri(fileUrl);
        var host = uri.Host;
        var path = uri.AbsolutePath;
        var port = 443; // HTTPS port

        // Create a TCP connection to the server
        using (var client = new TcpClient(host, port))
        {
            // Establish SSL/TLS connection
            var sslStream = new SslStream(client.GetStream(), false);

            // new RemoteCertificateValidationCallback((sender, certificate, chain, sslPolicyErrors) => true)

            sslStream.AuthenticateAsClient(host);

            // Prepare the HTTP GET request to download the file
            var request = $"GET {path} HTTP/1.1\r\n" +
                          $"Host: {host}\r\n" +
                          "Connection: close\r\n\r\n";
            var requestBytes = Encoding.ASCII.GetBytes(request);

            sslStream.Write(requestBytes, 0, requestBytes.Length);

            // Read the response (headers + body)
            using (var responseStream = new MemoryStream())
            {
                var buffer = new byte[1024];
                int bytesRead;
                var headerEnded = false;

                while ((bytesRead = sslStream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    responseStream.Write(buffer, 0, bytesRead);
                    var responseText = Encoding.ASCII.GetString(responseStream.ToArray());

                    // Check if we've passed the headers and reached the body (file data)
                    if (!headerEnded && responseText.Contains("\r\n\r\n"))
                    {
                        headerEnded = true;
                        // Trim the headers part, keeping only the body
                        var index = responseText.IndexOf("\r\n\r\n") + 4;
                        responseStream.Seek(index, SeekOrigin.Begin);
                    }
                }

                // Write the file to the local path
                File.WriteAllBytes(localPath, responseStream.ToArray());
            }
        }

        Console.WriteLine("File downloaded successfully.");
    }

    public static async Task DownloadFilesConcurrentlyAsync(string fileUrl, string localPath, int times)
    {
        // Create a list of download tasks
        var tasks = new List<Task>();

        for (var i = 0; i < times; i++)
        {
            // Create a unique file path for each download (e.g., appending an index to the file name)
            var indexedPath = $"{localPath}_{i + 1}";

            // Add each download task to the list
            tasks.Add(DownloadFileAsync(fileUrl, indexedPath));
        }

        // Run all download tasks concurrently
        await Task.WhenAll(tasks);

        Console.WriteLine("All files downloaded successfully.");
    }
}