using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Data;
using FirmsChemVS.Services.Repositories;
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
        public double CalculateMolecularWeight(List<ElementGridBinder> elements)
        {
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
            return Math.Round((abundance / totalAbundances), 5);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="total">I the total number of elements</param>
        /// <param name="elmentName"></param>
        /// <param name="elementLoc"></param>
        /// <param name="rowElements">The number of tomes that element is used</param>
        /// <param name="possibleIsotopesPerElement2"> The isotopes </param>
        /// <param name="counts"></param>
        /// <param name="startIndex"></param>
        /// <param name="totalAmount"></param>
        /// <param name="outputResults"></param>
        /// <param name="elements"></param>
        /// <param name="x"></param>
        /// <param name="y"></param>
        public void isotopeCombinations(int total, string[] elmentName, int elementLoc, Dictionary<string, int> rowElements,
                                        Dictionary<string, int[]> possibleIsotopesPerElement2, int[] counts, int startIndex,
                                        int totalAmount, List<Dictionary<int, int>> outputResults, string elements, int x, int y)
        {
            //Minimum cover
            int minimumCover = 0;
            int maximumCover = 0;
            //System.Console.WriteLine("Total :" + total);
            List<int> farthest = new List<int>();
            int prev = 0;
            for (int i = total - 1; i >= 0; i--)
            {
                int[] temp;
                int val;

                //System.Console.WriteLine("Elements :" + elmentName[i]);
                if (possibleIsotopesPerElement2.TryGetValue(elmentName[i], out temp) &&
                                                                rowElements.TryGetValue(elmentName[i], out val))
                {
                    //System.Console.WriteLine("Val :" + val);
                    minimumCover += temp[temp.Length - 1] * val;
                    maximumCover += temp[0] * val;
                    farthest.Add(prev + temp[0] * val);
                    prev += temp[0] * val;
                }
            }
            //System.Console.WriteLine("Minimum Cover :" + minimumCover);
            //System.Console.WriteLine("Maximum Cover :" + maximumCover);
            //System.Console.WriteLine("Isotope Cover :" + totalAmount);
            //Store a look of table of possible differences that the number can produce
            int distance = totalAmount - minimumCover;
            //System.Console.WriteLine("Difference :" + distance);
            //System.Console.WriteLine("Total :" + totalAmount);
            List<List<int>> theNum = new List<List<int>>();
            List<int> baseWeight = new List<int>();
            for (int j = 0; j < total; j++)
            {
                List<int> newSet = new List<int>();
                int[] temp;
                int val;
                if (possibleIsotopesPerElement2.TryGetValue(elmentName[j], out temp) &&
                                                                rowElements.TryGetValue(elmentName[j], out val))
                {
                    baseWeight.Add(temp[temp.Length - 1]);
                    for (int k = 0; k < temp.Count(); k++)
                    {
                        newSet.Add(temp.ElementAt(temp.Count() - k - 1) - temp[temp.Length - 1]);
                    }
                }
                theNum.Add(newSet);
            }

            List<int> validPath = new List<int>();
            dfs(theNum, distance, validPath, 0, 1, elmentName, rowElements, theNum.ElementAt(0).Count - 1, baseWeight, outputResults, farthest);


            /*for(int i = 0; theNum.Count > 1 && i < theNum.ElementAt(0).Count; i++)
            {
                System.Console.WriteLine("Difference :" + theNum.ElementAt(0).ElementAt(i));
            }*/

            /*
            List<string> valid = new List<string>();
            int[] input;
            if (possibleIsotopesPerElement2.TryGetValue("O", out input))
            {
                buildStrings(input, 52, valid, 3, 0, 0, "", 0);
                foreach (string l in valid)
                {
                    System.Console.WriteLine("Combo:" + l);
                }
                System.Console.WriteLine("");
                System.Console.WriteLine("");
            }*/
        }

        public void dfs(List<List<int>> theNum, int sum, List<int> validPath, int depth, int pos, string[] elmentName,
                      Dictionary<string, int> rowElements, int max, List<int> baseWeight, List<Dictionary<int, int>> outputResults, List<int> farthest)
        {
            int sum2 = 0;
            for (int i = 0; i < validPath.Count; i++)
            {
                if (validPath.ElementAt(i) == -1)
                    continue;
                sum2 += validPath.ElementAt(i);
            }
            if (sum2 > sum)
                return;
            if (depth < theNum.Count && farthest.ElementAt(depth) < sum - sum2)
                return;
            if (depth == theNum.Count)
            {

                if (sum2 == sum)
                {

                    //System.Console.Write("Valid :");
                    Dictionary<int, int> results = new Dictionary<int, int>();
                    int prev = -1;
                    int count = 0;
                    List<int> weight = new List<int>();
                    List<int> counting = new List<int>();
                    int loc = -1;
                    for (int i = 0; i < validPath.Count; i++)
                    {
                        if (prev == -1)
                        {
                            loc++;
                            prev = validPath.ElementAt(i);
                            count = 1;
                            continue;
                        }
                        if (validPath.ElementAt(i) == prev)
                        {
                            count++;
                            continue;
                        }
                        //System.Console.Write("Loc :" + loc + " " + baseWeight.Count + " ");
                        weight.Add(prev + baseWeight.ElementAt(loc));
                        counting.Add(count);
                        prev = validPath.ElementAt(i);
                        count = 1;
                    }
                    weight.Add(prev + baseWeight.ElementAt(loc));
                    counting.Add(count);

                    for (int i = 0; i < weight.Count; i++)
                    {
                        results.Add(weight.ElementAt(i), counting.ElementAt(i));
                    }

                    outputResults.Add(results);
                }
                return;
            }

            for (int i = max; i >= 0 /*< theNum.ElementAt(depth).Count*/; i--)
            {
                validPath.Add(theNum.ElementAt(depth).ElementAt(i));
                int val = 0;
                rowElements.TryGetValue(elmentName[depth], out val);
                //System.Console.WriteLine("Counts :" + val);
                if (pos == val)
                {
                    if (depth + 1 < theNum.Count)
                    {
                        validPath.Add(-1);
                        dfs(theNum, sum, validPath, depth + 1, 1, elmentName, rowElements, theNum.ElementAt(depth + 1).Count - 1, baseWeight, outputResults, farthest);
                        validPath.RemoveAt(validPath.Count - 1);
                    }
                    else
                    {
                        dfs(theNum, sum, validPath, depth + 1, 1, elmentName, rowElements, theNum.ElementAt(depth).Count - 1, baseWeight, outputResults, farthest);
                    }
                }
                else
                {
                    dfs(theNum, sum, validPath, depth, pos + 1, elmentName, rowElements, i, baseWeight, outputResults, farthest);
                }

                validPath.RemoveAt(validPath.Count - 1);
            }


        }




        public void buildStrings(int[] isotopes, int targetValue, List<string> valid, int elementCount,
                                    int pos, int arrayLocation, string currentName, int currentVal)
        {
            if (elementCount == pos)
            {
                if (currentVal == targetValue)
                    valid.Add(currentName);
                return;
            }
            for (; arrayLocation < isotopes.Length; arrayLocation++)
            {
                buildStrings(isotopes, targetValue, valid, elementCount, pos + 1, arrayLocation,
                                currentName + isotopes[arrayLocation].ToString(),
                                currentVal + isotopes[arrayLocation]);
            }
        }

        public void permuteTillTimeHasEnded(List<List<int>> theNum, int sum, int elements, int currSum, int currPos)
        {

            //if()
            // {

            //}


        }

        /*public void isotopeCombinations(int[] isotopes, int[] counts, int startIndex, int totalAmount,  List<Dictionary<int,int>> outputResults)
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
        }*/

        // determines the total combination for diving the mu(s)
        public double calculateIsotopeDividend(List<Dictionary<int, int>> partOfFraction, int[] isotopeNumbers)
        {
            var isotopeWithAbundances = isotopeService.getAllAbundancesByisotopeNumber(isotopeNumbers.ToList());
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




/*using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Data;
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
        public double CalculateMolecularWeight(List<ElementGridBinder> elements)
        {
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

        public void isotopeCombinations(List<int> isotopes, int[] counts, int startIndex, int totalAmount,  List<Dictionary<int,int>> outputResults)
        {
            Dictionary<string, int> totalAllowableIsotopes = new Dictionary<string, int>(); //keeps a total of the number of symbols that can be present for a given isotope comb.
            if (startIndex >= isotopes.Count)
            {
                //Check to see the total allowable in our current evaluated combination
                List<string> isotopeChars = isotopeService.getAllIsotopesInList(isotopes);  //Gets the isotope symbols by the isotope numbers

                //loops through each isotope symbol
                for (int index = 0; index < isotopeChars.Count; index++)
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
                    for (int index = 0; index < isotopes.Count; index++)
                    {
                        results.Add(isotopes[index], counts[index]);
                        //Console.Write(" ({0:N}:{1:N}) ", isotopes[index], counts[index]);
                    }
                    outputResults.Add(results);
                }
                //Console.WriteLine(" ");
                return;
            }

            if (startIndex == isotopes.Count - 1)
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
        public double calculateIsotopeDividend(List<Dictionary<int,int>> partOfFraction, List<int> isotopeNumbers)
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
}*/
