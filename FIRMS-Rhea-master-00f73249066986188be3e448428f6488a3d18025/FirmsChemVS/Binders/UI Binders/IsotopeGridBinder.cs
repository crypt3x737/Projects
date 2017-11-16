using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirmsChemVS
{
    public class IsotopeGridBinder : INotifyPropertyChanged, IDataGridBinder
    {
        public event PropertyChangedEventHandler PropertyChanged;

        private string _IsotopeFormula = string.Empty;
        private Dictionary<string, int> _ElementCount = new Dictionary<string, int>();
        private int? _ParentIonMass = 0;
        private double _DaughterIonMass = 0;
        private double _Abundance = 0;
        private int _NumOfIsotopes = 0;
        private double _CalculatedAbundance = 0;

        public string IsotopeFormula
        {
            get { return _IsotopeFormula; }
            set
            {
                _IsotopeFormula = value;

                NotifyPropertyChanged("IsotopeFormula");
            }
        }

        public Dictionary<string, int> ElementCount
        {
            get { return _ElementCount; }
            set { _ElementCount = value; }
        }

        public int? ParentIonMass
        {
            get { return _ParentIonMass; }
            set { _ParentIonMass = value; }
        }
        
        public double DaughterIonMass
        {
            get { return _DaughterIonMass; }
            set
            {
                _DaughterIonMass = value;
                NotifyPropertyChanged("DaughterIonMass");
            }
        }

        public double Abundance
        {
            get
            {
                return _Abundance;
            }
            set
            {
                _Abundance = value;
                NotifyPropertyChanged("Abundance");
            }
        }

        public int NumOfIsotopes
        {
            get
            {
                return _NumOfIsotopes;
            }
            set
            {
                _NumOfIsotopes = value;
                NotifyPropertyChanged("NumOfIsotopes");
            }
        }

        public double CalculatedAbundance
        {
            get
            {
                return _CalculatedAbundance;
            }
            set
            {
                _CalculatedAbundance = value;
                NotifyPropertyChanged("CalculatedAbundance");
            }
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
