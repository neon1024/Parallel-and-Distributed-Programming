using System.Net.Sockets;
using System.Text;

namespace Lab_4;

internal class FileDownloader1
{
    // Entry method to download a file using sockets
    public static void DownloadFileAsync(string fileUrl, string localPath)
    {
        var uri = new Uri(fileUrl);
        var host = uri.Host;
        var path = uri.AbsolutePath;
        var port = uri.Scheme == "https" ? 443 : 80; // Use HTTPS or HTTP port

        // Create a new socket and initiate connection asynchronously
        var socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        socket.BeginConnect(host, port, ConnectCallback, new DownloadState
        {
            Socket = socket,
            Host = host,
            Port = port,
            Path = path,
            LocalPath = localPath
        });
    }

    // Callback after connection is established
    private static void ConnectCallback(IAsyncResult ar)
    {
        var state = (DownloadState)ar.AsyncState;
        var socket = state.Socket;

        try
        {
            // Complete the connection process
            socket.EndConnect(ar);
            Console.WriteLine($"Connected to {state.Host} on port {state.Port}");

            // Prepare the HTTP GET request to download the file
            var request = $"GET {state.Path} HTTP/1.1\r\n" +
                          $"Host: {state.Host}\r\n" +
                          "Connection: close\r\n\r\n";
            var requestBytes = Encoding.ASCII.GetBytes(request);

            // Start sending the HTTP GET request asynchronously
            socket.BeginSend(requestBytes, 0, requestBytes.Length, SocketFlags.None, SendCallback, state);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error during connection: {ex.Message}");
        }
    }

    // Callback after the request is sent
    private static void SendCallback(IAsyncResult ar)
    {
        var state = (DownloadState)ar.AsyncState;
        var socket = state.Socket;

        try
        {
            // Complete the sending process
            socket.EndSend(ar);
            Console.WriteLine("Request sent, awaiting response...");

            // Start receiving the file content
            var buffer = new byte[1024];
            socket.BeginReceive(buffer, 0, buffer.Length, SocketFlags.None, ReceiveCallback, new ReceiveState
            {
                Socket = socket,
                Buffer = buffer,
                LocalPath = state.LocalPath
            });
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error during sending: {ex.Message}");
        }
    }

    // Callback after the response is received
    private static void ReceiveCallback(IAsyncResult ar)
    {
        var receiveState = (ReceiveState)ar.AsyncState;
        var socket = receiveState.Socket;

        try
        {
            // Read the received data
            var bytesRead = socket.EndReceive(ar);
            if (bytesRead > 0)
            {
                // Write the data to the local file
                using (var fs = new FileStream(receiveState.LocalPath, FileMode.Append, FileAccess.Write))
                {
                    fs.Write(receiveState.Buffer, 0, bytesRead);
                }

                // Continue receiving if there is more data
                socket.BeginReceive(receiveState.Buffer, 0, receiveState.Buffer.Length, SocketFlags.None, ReceiveCallback, receiveState);
            }
            else
            {
                // No more data, close the socket and finish the download
                socket.Close();
                Console.WriteLine($"File successfully downloaded to {receiveState.LocalPath}");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error during receiving: {ex.Message}");
        }
    }

    // Helper class to store download state for connection and sending
    private class DownloadState
    {
        public Socket Socket { get; set; }
        public string Host { get; set; }
        public int Port { get; set; }
        public string Path { get; set; }
        public string LocalPath { get; set; }
    }

    // Helper class to store receive state
    private class ReceiveState
    {
        public Socket Socket { get; set; }
        public byte[] Buffer { get; set; }
        public string LocalPath { get; set; }
    }
}