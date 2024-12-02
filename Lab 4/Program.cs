namespace Lab_4;

public static class Program
{
    public static void Main()
    {
        var fileUrl = "https://www.cs.ubbcluj.ro/~rlupsa/edu/pdp/progs/srv-begin-end.cs";
        var localPath = "C:\\Users\\rober\\Desktop\\downloaded_file"; // Specify the path to save the file

        // var timer = new Stopwatch();
        //
        // timer.Start();
        // FileDownloader.DownloadFile(fileUrl, localPath);
        // FileDownloader.DownloadFile(fileUrl, localPath);
        // FileDownloader.DownloadFile(fileUrl, localPath);
        // FileDownloader.DownloadFile(fileUrl, localPath);
        // FileDownloader.DownloadFile(fileUrl, localPath);
        // timer.Stop();
        //
        // Console.Out.WriteLine($"Sequential execution elapsed time for 5 downloads: {timer.Elapsed.Minutes} Minutes, {timer.Elapsed.Seconds} Seconds, {timer.Elapsed.Milliseconds} Milliseconds");
        //
        // Task[] tasks = new Task[5];
        //
        // for (var i = 0; i < 5; ++i) tasks[i] = FileDownloader.DownloadFileAsync(fileUrl, localPath + i);
        //
        // timer.Restart();
        // Task.WaitAll(tasks);
        // timer.Stop();
        //
        // Console.Out.WriteLine($"Concurrent execution elapsed time for 5 downloads: {timer.Elapsed.Minutes} Minutes, {timer.Elapsed.Seconds} Seconds, {timer.Elapsed.Milliseconds} Milliseconds");

        Console.Out.WriteLine("Downloading file using FileDownloader1...");
        FileDownloader1.DownloadFileAsync(fileUrl, localPath);
        Console.Out.WriteLine("Downloaded file using FileDownloader1...");
    }
}