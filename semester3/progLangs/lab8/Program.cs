using part01;
using System.Reflection;

var engine = new SkillEngine();

// Регистрация текущей сборки (или загрузка внешней DLL)
engine.RegisterAssembly(Assembly.GetExecutingAssembly());

// Симуляция контекста боя
var context = new BattleContext
{
    DamageDealt = 100,
    Attacker = new UnitStats { Hp = 100 },
    Defender = new UnitStats { Hp = 150 }
};

Console.WriteLine("--- Starting Defense Phase ---");
engine.ExecutePipeline(TriggerType.OnDefense, context);
Console.WriteLine($"Attacker HP: {context.Attacker.Hp}");
Console.WriteLine($"Defender HP: {context.Defender.Hp}");

Console.WriteLine("--- Starting Attack Phase ---");
engine.ExecutePipeline(TriggerType.OnAttack, context);
Console.WriteLine($"Attacker HP: {context.Attacker.Hp}");
Console.WriteLine($"Defender HP: {context.Defender.Hp}");

Console.WriteLine("--- Starting PostBattle Phase ---");
engine.ExecutePipeline(TriggerType.PostBattle, context);
Console.WriteLine($"Attacker Final HP: {context.Attacker.Hp}");
Console.WriteLine($"Defender Final HP: {context.Defender.Hp}");
