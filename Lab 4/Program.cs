namespace Lab_4;

public static class Program
{
    public static void Main()
    {
        var fileUrl = "https://www.cs.ubbcluj.ro/~rlupsa/edu/pdp/progs/srv-begin-end.cs";
        var localPath = "C:\\Users\\rober\\Desktop\\downloaded_file"; // Specify the path to save the file
        FileDownloader.DownloadFile(fileUrl, localPath);
    }
}