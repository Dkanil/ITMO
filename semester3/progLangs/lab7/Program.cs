using System.Runtime.InteropServices;


[DllImport("point_lib.dll", CallingConvention = CallingConvention.Cdecl)]
static extern int filter_array([In] Point[] arr, int len, [Out] Point[] res, MyFilter filter_point);

void GenerateTestFile(string filePath, int arrLen, int rangeMin, int rangeMax)
{
    Random random = new Random();

    using (StreamWriter writer = new StreamWriter(filePath))
    {
        for (int i = 0; i < arrLen; i++)
        {
            int x1 = random.Next(rangeMin, rangeMax + 1);
            int y1 = random.Next(rangeMin, rangeMax + 1);

            writer.WriteLine($"{x1},{y1}");
        }
    }
}

Point[] ReadPointsFromFile(string filePath)
{
    var points = new List<Point>();

    using (StreamReader reader = new StreamReader(filePath))
    {
        string line;
        while ((line = reader.ReadLine()) != null)
        {
            var values = line.Trim().Replace(',', ' ').Split();
            if (values.Length == 2)
            {
                int x1 = int.Parse(values[0]);
                int y1 = int.Parse(values[1]);

                points.Add(new Point { x = x1, y = y1 });
            }
        }
    }

    return points.ToArray();
}


string filePath = "test.txt";
int arrLen = 10;
int rangeMin = -1000;
int rangeMax = 1000;
GenerateTestFile(filePath, arrLen, rangeMin, rangeMax);

Point[] points = ReadPointsFromFile(filePath);


void PrintPoints(string quarterName, Point[] points, int size)
{
    Console.WriteLine($"Points in the {quarterName}:");
    for (int i = 0; i < size; i++)
    {
        Console.WriteLine($"({points[i].x}, {points[i].y})");
    }
}

Point[] res = new Point[arrLen];

int firstQuarterSize = filter_array(points, arrLen, res, p => ((p.x > 0) && (p.y > 0)) ? 1 : 0);
PrintPoints("first quarter", res, firstQuarterSize);

int secondQuarterSize = filter_array(points, arrLen, res, p => ((p.x < 0) && (p.y > 0)) ? 1 : 0);
PrintPoints("second quarter", res, secondQuarterSize);

int thirdQuarterSize = filter_array(points, arrLen, res, p => ((p.x < 0) && (p.y < 0)) ? 1 : 0);
PrintPoints("third quarter", res, thirdQuarterSize);

int fourthQuarterSize = filter_array(points, arrLen, res, p => ((p.x > 0) && (p.y < 0)) ? 1 : 0);
PrintPoints("fourth quarter", res, fourthQuarterSize);

[UnmanagedFunctionPointer(CallingConvention.Cdecl)]
public delegate int MyFilter(Point p);