public class HealingPotion : IArtifact
{
    private int healAmount;
    public HealingPotion(int healAmount)
    {
        this.healAmount = healAmount;
    }
    public void Use(Hero owner, Hero target)
    {
        owner.HP += healAmount;
        Console.WriteLine(owner.Name + " использует HealingPotion и восстанавливает " + healAmount + " HP");
    }
}

