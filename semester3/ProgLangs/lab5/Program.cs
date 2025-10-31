var tasks = args.Select(async fileName =>
{
    try
    {
        var content = await File.ReadAllTextAsync(fileName);
        var wordCount = content.Split([' ', '\n'], StringSplitOptions.RemoveEmptyEntries).Length;
        Console.WriteLine($"File: {fileName}, Word Count: {wordCount}");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"File: {fileName}, Error: {ex.Message}");
    }
});
await Task.WhenAll(tasks);
