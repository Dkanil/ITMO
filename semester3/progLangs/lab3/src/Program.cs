internal class Program
{
    static void Main(string[] args)
    {
        Knight knight = new Knight("Arthur", 100, 15, 10, new HealingPotion(20));
        Wizard wizard = new Wizard("Harry Potter", 80, 20, 5);
        BattleManager battleManager = new BattleManager();
        battleManager.StartBattle(knight, wizard);
    }
}
