public class BattleManager
{
    private Hero attack(Hero attacker, Hero defender, int turn)
    {
        if (turn % 3 == 0)
        {
            attacker.SpecialAbility(defender);
            Console.WriteLine(attacker.Name + " атакует " + defender.Name);
        }
        else
        {
            Random rand = new Random();
            if ((attacker.Artifact != null) && (rand.Next(100) <= 25))
            {
                Console.WriteLine(attacker.Name + " использует артефакт");
                attacker.Artifact.Use(attacker, defender);
            } else
            {
                attacker.Attack(defender);
                Console.WriteLine(attacker.Name + " атакует " + defender.Name);
            }
        }
        if (defender.HP <= 0)
        {
            Console.WriteLine(attacker.Name + " победил!");
            return attacker;
        }
        Console.WriteLine("Здоровье " + attacker.Name + " = " + attacker.HP + "; Здоровье " + defender.Name + " = " + defender.HP);
        return null;
    }
    public void StartBattle(Hero hero1, Hero hero2)
    {
        Console.WriteLine("Битва начинается между " + hero1.Name + " и " + hero2.Name);
        Console.WriteLine(hero1.Name + ": Здоровье " + hero1.HP + ", Атака " + hero1.AttackPower + ", Защита " + hero1.Defense);
        Console.WriteLine(hero2.Name + ": Здоровье " + hero2.HP + ", Атака " + hero2.AttackPower + ", Защита " + hero2.Defense);

        int turn = 1;
        Hero winner = null;
        while (winner == null)
        {
            Console.WriteLine();
            Console.WriteLine("Ход " + turn);
            if (attack(hero1, hero2, turn) != null)
            {
                winner = hero1;
                break;
            }

            winner = attack(hero2, hero1, turn);
            turn++;
        }

        string docPath = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments);
        string logFilePath = Path.Combine(docPath, "battle_log.txt");
        string logContent = $"Битва завершена! Победитель: {winner.Name} с {winner.HP} HP.";
        File.WriteAllText(logFilePath, logContent);
        Console.WriteLine($"Лог боя сохранен в: {logFilePath}");
    }
}
