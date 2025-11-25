using System.Runtime.InteropServices;

public class Knight : Hero
{
    public Knight(string name, int hp, int attackPower, int defense) : base(name, hp, attackPower, defense)
    {
    }    
    public Knight(string name, int hp, int attackPower, int defense, IArtifact artifact) : base(name, hp, attackPower, defense, artifact)
    {
    }
    public override void SpecialAbility(Hero target)
    {
        if (RuntimeInformation.IsOSPlatform(OSPlatform.Windows))
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine($"{Name} удваивает урон!");
            Console.ResetColor();
            Console.Beep(300, 800);
        }
        else if (RuntimeInformation.IsOSPlatform(OSPlatform.Linux) || RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
        {
            Console.WriteLine($"\x1b[33m{Name} удваивает урон!\x1b[0m");
            Console.WriteLine("\a");
        }
        target.TakeDamage((this.AttackPower - target.Defense) * 2);
    }
}
