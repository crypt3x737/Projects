using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace FirmsChemVS
{
    public class CountToBooleanConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            // Do the conversion from bool to visibility
            bool isEnabled = false;

            if ((int)value > 1)
            {
                isEnabled = true;
            }

            return isEnabled;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            // Do the conversion from visibility to bool
            return null;
        }
    }
}
