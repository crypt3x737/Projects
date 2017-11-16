using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirmsChemVS
{
    public class ElementGridBinder : INotifyPropertyChanged
    {
        private int _Count = 0;
        public event PropertyChangedEventHandler PropertyChanged;

        public string Symbol { get; set; }

        public int Count
        {
            get { return _Count; }
            set
            {
                _Count = value;
                NotifyPropertyChanged("Count");
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
