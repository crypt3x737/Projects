using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace FirmsChemVS
{
    /// <summary>
    /// Interaction logic for IsotopeChooser.xaml
    /// </summary>
    public partial class IsotopeChooser : Window, INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public Dictionary<string, int> ElementCounter { get; private set; }
        public string IsotopeFormula { get; private set; }
        private Dictionary<string, Button> _ElementButtons { get; set; }
        private List<ElementGridBinder> _ElementList { get; set; }

        public IsotopeChooser(List<ElementGridBinder> elementList, string oldVal)
        {
            InitializeComponent();

            ElementCounter = new Dictionary<string, int>();
            _ElementButtons = new Dictionary<string, Button>();
            _ElementList = elementList;
            
            IsotopeFormula = oldVal;

            LayoutWindow(elementList);
        }

        private void LayoutWindow(List<ElementGridBinder> elementList)
        {
            foreach (ElementGridBinder egb in elementList)
            {
                if (egb.Count > 0)
                {
                    Button element = new Button();
                    element.Content = egb.Symbol;
                    element.Margin = new Thickness(10, 10, 10, 10);
                    element.Width = 40;
                    element.Height = 40;
                    element.Click += Element_Click;

                    _ElementButtons[egb.Symbol] = element;
                    stpElements.Children.Add(element);
                }
            }
        }

        private void Element_Click(object sender, RoutedEventArgs e)
        {
            int elmCount = 0;
            string isotopeFormula = string.Empty;
            Button buttonClicked = (Button)sender;
            string element = buttonClicked.Content.ToString();

            if (!ElementCounter.TryGetValue(element, out elmCount))
            {
                ElementCounter[element] = 1;
            }
            else
            {
                ElementCounter[element] += 1;
            }

            if (ElementCounter[element] == _ElementList.Find(el => el.Symbol == element).Count)
            {
                _ElementButtons[element].IsEnabled = false;
            }

            foreach (string elmt in ElementCounter.Keys)
            {
                if (ElementCounter[elmt] == 1)
                {
                    isotopeFormula += elmt;
                }
                else
                {
                    isotopeFormula += elmt + ElementCounter[elmt];
                }
            }

            lblIsotope.Content = isotopeFormula;
        }

        private void btnSetIsotope_Click(object sender, RoutedEventArgs e)
        {
            IsotopeFormula = lblIsotope.Content.ToString();

            this.Close();
        }

        private void btnResetIsotope_Click(object sender, RoutedEventArgs e)
        {
            lblIsotope.Content = string.Empty;
            IsotopeFormula = string.Empty;
            ElementCounter.Clear();

            foreach (UIElement btn in stpElements.Children)
            {
                (btn as Button).IsEnabled = true;
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
