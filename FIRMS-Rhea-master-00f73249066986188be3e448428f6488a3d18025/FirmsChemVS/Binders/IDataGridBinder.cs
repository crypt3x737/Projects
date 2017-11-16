using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirmsChemVS
{
    public interface IDataGridBinder
    {
        double DaughterIonMass { get; set; }
        double Abundance { get; set; }
    }
}
