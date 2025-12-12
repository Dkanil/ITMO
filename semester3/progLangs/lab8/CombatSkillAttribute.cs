using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace part01
{
    //Этот атрибут нужен для упрощения поиска классов, реализующих логику игры
    [AttributeUsage(AttributeTargets.Class, Inherited = false, AllowMultiple = false)]
    public class GameAttribute : Attribute    {    }

    /// <summary>
    /// Атрибут-маркер, превращающий обычный метод в исполняемую единицу бизнес-логики.
    /// </summary>
    [AttributeUsage(AttributeTargets.Method, Inherited = false, AllowMultiple = false)]
    public class CombatSkillAttribute : Attribute
    {
        /// <summary>Уникальный идентификатор навыка в системе.</summary>
        public string Name { get; }

        /// <summary>Точка внедрения в пайплайн обработки.</summary>
        public TriggerType Trigger { get; }

        /// <summary>Приоритет выполнения (выше значение -> раньше выполнение).</summary>
        public int Priority { get; }

        public CombatSkillAttribute(string name, TriggerType trigger, int priority = 1)
        {
            Name = name;
            Trigger = trigger;
            Priority = priority;
        }
    }

}
