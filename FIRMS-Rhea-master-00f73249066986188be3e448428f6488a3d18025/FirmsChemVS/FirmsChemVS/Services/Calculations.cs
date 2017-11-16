using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Data;
using FirmsChemVS.Repositories;
/// <summary>
/// This file sounds like it handles most of the calculations required for the chemistry
/// part of this program.  
/// </summary>
namespace FirmsChemVS.Services
{
    /// <summary>
    /// This class is the class which handles most of the
    /// calculations for the chemistry part of the program.  
    /// It's a singleton. ;)
    /// </summary>
    public class Calculations
    {
        IsotopeService isotopeService = IsotopeService.Instance;
        private static Calculations instance;
        private Dictionary<string, int> currentRowElements = null;
        /// <summary>
        /// Instance property for this singleton
        /// </summary>
        public static Calculations Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new Calculations();
                }
                return instance;
            }
        }
        /// <summary>
        /// CurrentRowElements property for this class
        /// This contains both the getter and the setter.
        /// I imagine this has to do with the data grid view
        /// from the forms
        /// </summary>
        public Dictionary<string, int> CurrentRowElements
        {
            get
            {
                return this.currentRowElements;
            }
            set
            {
                this.currentRowElements = value;
            }
        }
        // Given the molecular formula the atomic weight is retrieved from the database to calculate the molecular weight
        public double CalculateMolecularWeight(DataTable elements)
        {
            /*double molecularWeight = 0;
            using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
            {
                foreach(DataRow row in elements.Rows)
                {
                    if (row[1] as string == "0")
                    {
                        continue;
                    }
                    string symbol = row[0] as string;
                    if (Convert.ToInt32(row[1]) > 0) { 
                    var query = ((from isotope in db.Isotopes
                                 where isotope.atomSymbol == symbol
                                 select isotope).OrderByDescending(i => i.abundance)).First();
                    molecularWeight += (double)query.atomMass * Convert.ToInt32(row[1]);
                    }
                }
                
            }
            return molecularWeight;*/
            return isotopeService.CalculateMolecularWeight(elements);
        }

        // using the information given on the number of each element present in the compound
        // the molecular formula is returned as a string in a textbox.
        public string CalculateMolecularFormula(DataTable elements)
        {
            String formula = "";
            foreach (DataRow entry in elements.Rows)
            {
                if (Convert.ToInt32(entry[1]) > 0)
                {
                    if (Convert.ToInt32(entry[1]) == 1)
                    {
                        formula += entry[0];
                    }
                    else
                    {
                        formula += (string)entry[0] + (string)entry[1];
                    }
                }
            }
            return formula;
        }
      

        public double calculateAlphaForRow(double totalAbundances, double abundance)
        {
            return Math.Round((abundance / totalAbundances),5);
        }

        public void isotopeCombinations(int[] isotopes, int[] counts, int startIndex, int totalAmount,  List<Dictionary<int,int>> outputResults)
        {
            Dictionary<string, int> totalAllowableIsotopes = new Dictionary<string, int>(); //keeps a total of the number of symbols that can be present for a given isotope comb.
            if (startIndex >= isotopes.Length)
            {
                //Check to see the total allowable in our current evaluated combination
                string[] isotopeChars = isotopeService.getAllIsotopesInList(isotopes);  //Gets the isotope symbols by the isotope numbers
                
                //loops through each isotope symbol
                for (int index = 0; index < isotopeChars.Length; index++)
                {
                    int retrievedValue;
                    
                    if (totalAllowableIsotopes.TryGetValue(isotopeChars[index], out retrievedValue)) //if the key doesn't exist then return true and populate retrieved value with 0
                    {
                        totalAllowableIsotopes[isotopeChars[index]] = retrievedValue + counts[index]; //adds 0 + the combinations total for a specific symbol
                    }
                    else
                    {

                        totalAllowableIsotopes.Add(isotopeChars[index], counts[index]);
                    }
                }

                var dict3 = CurrentRowElements.Where(entry => totalAllowableIsotopes[entry.Key] != entry.Value)
                 .ToDictionary(entry => entry.Key, entry => entry.Value) as Dictionary<string, int>; //compare the current isotope with the allowable isotope we got from the combination
               
                if (dict3.Count == 0) //if it is not equal to 0 it is not a valid combination.
                {
                    Dictionary<int, int> results = new Dictionary<int, int>();
                    for (int index = 0; index < isotopes.Length; index++)
                    {
                        results.Add(isotopes[index], counts[index]);
                        //Console.Write(" ({0:N}:{1:N}) ", isotopes[index], counts[index]);
                    }
                    outputResults.Add(results);
                }
                //Console.WriteLine(" ");
                return;
            }

            if (startIndex == isotopes.Length - 1)
            {
                if (totalAmount % isotopes[startIndex] == 0)
                {
                    counts[startIndex] = totalAmount / isotopes[startIndex];
                    isotopeCombinations(isotopes, counts, startIndex + 1, 0, outputResults);
                }
            }

            else
            {
                for (int i = 0; i <= totalAmount / isotopes[startIndex]; i++)
                {
                    counts[startIndex] = i;
                    isotopeCombinations(isotopes, counts, startIndex + 1, totalAmount - isotopes[startIndex] * i,outputResults);
                }
            }
        }

        // determines the total combination for diving the mu(s)
        public double calculateIsotopeDividend(List<Dictionary<int,int>> partOfFraction, int[] isotopeNumbers)
        {
            var isotopeWithAbundances = isotopeService.getAllAbundancesByisotopeNumber(isotopeNumbers);
            double total = 0.0;
            foreach (Dictionary<int, int> calcItem in partOfFraction)
            {
                double acc = 1;

                foreach (KeyValuePair<int, int> element in calcItem)
                {
                    acc *= Math.Pow(isotopeWithAbundances[element.Key] / 100, element.Value);
                }
                total += acc;
                acc = 1;
            }

            return total;
        }
    }
}
