public abstract class Hero
{
    public string Name { get; set; }
    public int HP { get; set; }
    public int AttackPower { get; set; }
    public int Defense { get; set; }
    public IArtifact Artifact { get; set; }
    public Hero(string name, int hp, int attackPower, int defense)
    {
        Name = name;
        HP = hp;
        AttackPower = attackPower;
        Defense = defense;
        Artifact = null;
    }

    public Hero(string name, int hp, int attackPower, int defense, IArtifact artifact)
    {
        Name = name;
        HP = hp;
        AttackPower = attackPower;
        Defense = defense;
        Artifact = artifact;
    }

    public void TakeDamage(int damage)
    {
        if (damage - Defense < 0)
            damage = 0;
        HP = Math.Max(HP - damage, 0);
    }

    public void Attack(Hero target)
    {
        target.TakeDamage(this.AttackPower - target.Defense);
    }

    public abstract void SpecialAbility(Hero target);
}