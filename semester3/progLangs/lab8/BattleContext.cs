using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace part01
{
    // Контекст исполнения: данные, доступные внутри навыка
    public class BattleContext
    {
        public int DamageDealt { get; set; }
        public UnitStats Attacker { get; set; }
        public UnitStats Defender { get; set; }
    }
}
