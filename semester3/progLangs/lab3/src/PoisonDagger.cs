public class PoisonDagger : IArtifact
{
    private int damage;
    public PoisonDagger(int damage)
    {
        this.damage = damage;
    }
    public void Use(Hero owner, Hero target)
    {
        target.TakeDamage(damage);
        Console.WriteLine(owner.Name + " использует PoisonDagger против " + target.Name + ", нанося " + damage + " урона");
    }
}
