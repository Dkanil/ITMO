using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace part01
{
    [GameAttribute]
    public class KnightMechanics
    {
        private Func<int, int, int> reduceDamage()
        {
            var dynamicMethod = new DynamicMethod("reduceDamage", typeof(int), [typeof(int), typeof(int)]);
            ILGenerator il = dynamicMethod.GetILGenerator();
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Ldarg_0);
            il.Emit(OpCodes.Ldc_I4_2);
            il.Emit(OpCodes.Div);
            il.Emit(OpCodes.Sub);
            il.Emit(OpCodes.Ret);

            return (Func<int, int, int>)dynamicMethod.CreateDelegate(typeof(Func<int, int, int>));
        }

        [CombatSkill("ShieldWall", TriggerType.OnDefense, 1)]
        public void ExecuteShieldWall(BattleContext ctx)
        {
            var fastDelegate = reduceDamage();
            ctx.DamageDealt = fastDelegate(ctx.DamageDealt, ctx.DamageDealt);
            Console.WriteLine($"[System] ShieldWall activation: reduced damage by 50%. Current damage is {ctx.DamageDealt}");
        }

        private Func<int, int, int> decreaseHp()
        {
            var dynamicMethod = new DynamicMethod("reduceDamage", typeof(int), [typeof(int), typeof(int)]);
            ILGenerator il = dynamicMethod.GetILGenerator();
            il.Emit(OpCodes.Ldarg_0);
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Sub);
            il.Emit(OpCodes.Ret);

            return (Func<int, int, int>)dynamicMethod.CreateDelegate(typeof(Func<int, int, int>));
        }

        [CombatSkill("LastAttack", TriggerType.PostBattle, 5)]
        public void ExecuteLastAttack(BattleContext ctx)
        {
            int damageToBoth = 10;
            var fastDelegate = decreaseHp();
            ctx.Attacker.Hp = fastDelegate(ctx.Attacker.Hp, damageToBoth);
            ctx.Defender.Hp = fastDelegate(ctx.Defender.Hp, damageToBoth);
            Console.WriteLine($"[System] LastAttack activation: both units take {damageToBoth} damage.");
        }
    }
}
