using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirmsChemVS
{
    public class IonMassGridBinder : INotifyPropertyChanged, IDataGridBinder
    {
        private double _Alpha = 0;
        private double _DaughterIonMass = 0;
        private double _Abundance = 0;
        public event PropertyChangedEventHandler PropertyChanged;

        public int? BaseIonMass { get; set; }
        public double DaughterIonMass
        {
            get { return _DaughterIonMass; }
            set { _DaughterIonMass = value; NotifyPropertyChanged("DaughterIonMass"); }
        }
        public double Abundance
        {
            get { return _Abundance; }
            set { _Abundance = value; NotifyPropertyChanged("Abundance"); }
        }
        public double Alpha
        {
            get { return _Alpha; }
            set { _Alpha = value; NotifyPropertyChanged("Alpha"); }
        }

        private void NotifyPropertyChanged(string propertyName = "")
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}
