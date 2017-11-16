using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
/// <summary>
/// This file is the main starting file for the WinForm version of the application.
/// We will be switching to WPF so this becomes obsolete
/// </summary>
namespace FirmsChemVS
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            //System.Console.WriteLine("Hello, World!");
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }
    }
}
