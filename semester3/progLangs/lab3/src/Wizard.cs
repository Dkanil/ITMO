using System.Runtime.InteropServices;
public class Wizard : Hero
{
    public Wizard(string name, int hp, int attackPower, int defense) : base(name, hp, attackPower, defense)
    {
    }
    public Wizard(string name, int hp, int attackPower, int defense, IArtifact artifact) : base(name, hp, attackPower, defense, artifact)
    {
    }
    public override void SpecialAbility(Hero target)
    {
        if (RuntimeInformation.IsOSPlatform(OSPlatform.Windows))
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine($"{Name} кастует огненный шар!");
            Console.ResetColor();
            Console.Beep(1000, 100);
            Console.Beep(1000, 100);
            Console.Beep(1000, 100);
            Console.Beep(1000, 800);
        }
        else if (RuntimeInformation.IsOSPlatform(OSPlatform.Linux) || RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
        {
            Console.WriteLine($"\x1b[31m{Name} кастует огненный шар!\x1b[0m");
            Console.WriteLine("\a");
        }
        target.TakeDamage(this.AttackPower);
    }
}

