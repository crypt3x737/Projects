using System;
using System.Collections.Generic;
using System.Linq;
using System.Data;
using FirmsChemVS.Services.Repositories;

namespace FirmsChemVS.Services
{
    public class IsotopeService
    {
        //private static System.Collections.GenericOrderByDescending.List<Repositories.Isotope>[] lookUpTableString;
        //private static string[] lookUpTableNumber;
        //private static double[] lookUpTableDict;
        private static List<Isotope> lookUpIsotopes;

        private static IsotopeService instance;

        public static IsotopeService Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new IsotopeService();

                    using (FirmsChemDBEntities db = new FirmsChemDBEntities())
                    {
                        var query = (from isotope in db.Isotopes
                                      where "Br" == "Br"
                                      select isotope).ToList();
                        lookUpIsotopes = query;
                        db.Dispose();
                    }
                }
                return instance;
            }
        }


        public List<Isotope> getAllIsotopesForAtom(string symbol)
        {
            List<Isotope> listOfIsotopes = new List<Isotope>();

            listOfIsotopes = lookUpIsotopes.FindAll(e => e.atomSymbol == symbol);

            return listOfIsotopes.OrderByDescending(i => i.abundance).ToList();
        }
        
        public List<string> getAllIsotopesInList(List<int> isotopes)
        {
            List<string> listOfIsotopes = new List<string>();

            foreach (int next in isotopes)
            {
                foreach (Isotope curr in lookUpIsotopes)
                {
                    if (curr.isotopeNumber == next)
                    {
                        listOfIsotopes.Add(curr.atomSymbol);
                    }
                }
            }
            
            return listOfIsotopes;
        }

        public Dictionary<int, double> getAllAbundancesByisotopeNumber(List<int> isotopeNumbers)
        {
            Dictionary<int, double> isotopeNumbersWithAbundance = new Dictionary<int, double>();
            
            foreach (int next in isotopeNumbers)
            {
                foreach (Isotope curr in lookUpIsotopes)
                {
                    if (curr.isotopeNumber == next)
                    {
                        isotopeNumbersWithAbundance.Add(curr.isotopeNumber.GetValueOrDefault(), curr.abundance.GetValueOrDefault());
                    }
                }
            }

            return isotopeNumbersWithAbundance;
        }

        public List<int> getAllIsotopesInIsotopeSymbols(string[] atomSymbol)
        {
            List<int> isotopes = new List<int>();

            foreach (string next in atomSymbol)
            {
                foreach (Isotope curr in lookUpIsotopes)
                {
                    if (curr.atomSymbol == next)
                    {
                        isotopes.Add(curr.isotopeNumber.GetValueOrDefault());
                    }
                }
            }
            
            return isotopes;
        }
        
        public double CalculateMolecularWeight(List<ElementGridBinder> elements)
        {
            double molecularWeight = 0;

            foreach (ElementGridBinder elementBinder in elements)
            {
                if (elementBinder.Count > 0)
                {
                    int count = 0;
                    List<Isotope> tempTable = new List<Isotope>();

                    foreach (Isotope curr in lookUpIsotopes)
                    {
                        if (curr.atomSymbol == elementBinder.Symbol)
                        {

                            count++;
                            tempTable.Add(curr);
                        }
                    }

                    tempTable = tempTable.OrderByDescending(i => i.abundance).ToList();
                    molecularWeight += tempTable.First().atomMass.Value * elementBinder.Count;
                }
            }
            
            return molecularWeight;
        }

        public int CalculateIonMass(Dictionary<string, int> elements)
        {
            int isotopeWeight = 0;

            foreach (string symbol in elements.Keys)
            {
                int count = 0;
                List<Isotope> tempTable = new List<Isotope>();

                foreach (Isotope curr in lookUpIsotopes)
                {
                    if (curr.atomSymbol == symbol)
                    {
                        count++;
                        tempTable.Add(curr);
                    }
                }

                tempTable = tempTable.OrderByDescending(i => i.abundance).ToList();

                isotopeWeight += tempTable.First().isotopeNumber.Value * elements[symbol];
            }

            return isotopeWeight;
        }


        public void getAllAbundancesInIsotopeSymbols(Dictionary<string, int> atomList, Abundance data)
        {
            //Determine initial guess, as well as the maximum possible value and minimum possible value.
            double[] isotopes;
            List<Double> max = new List<Double>();
            List<Double> min = new List<Double>();
            List<Double> mid = new List<Double>();

            Random rng = new Random();

            foreach (KeyValuePair<string, int> entry in atomList)
            {
                int pos = 0;
                foreach (Isotope curr in lookUpIsotopes)
                    if (entry.Key.Equals(curr.atomSymbol))
                        pos++;
                isotopes = new double[pos];
                pos = 0;
                foreach (Isotope curr in lookUpIsotopes)
                {
                    if (entry.Key.Equals(curr.atomSymbol))
                    {
                        isotopes[pos] = curr.abundance.GetValueOrDefault();
                        pos++;
                    }
                }



                for (int i = 0; i < entry.Value; i++)
                {



                    //Initial guess information determined HERE.
                    //add to list
                    max.Add(0.90);//isotopes[0] / 100.00);
                    min.Add(0.001);//isotopes[1] / 100.00);

                    //MIDDLE GUESS....
                    mid.Add(0.55);//rng.NextDouble( isotopes[1] / 100.00, isotopes[0] / 100.00) );
                }
            }

            /*using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())

               
                 foreach (KeyValuePair<string, int> entry in atomList)
                 {
                    isotopes = (from isotope in db.Isotopes
                                where entry.Key.Equals(isotope.atomSymbol)
                                orderby isotope.abundance descending
                                select isotope.abundance ?? 0).ToArray();
                    for (int i = 0; i < entry.Value; i++)
                    {
                        


                        //Initial guess information determined HERE.
                        //add to list
                        max.Add(0.90);//isotopes[0] / 100.00);
                        min.Add(0.001);//isotopes[1] / 100.00);

                        //MIDDLE GUESS....
                        mid.Add(0.55);//rng.NextDouble( isotopes[1] / 100.00, isotopes[0] / 100.00) );
                    }
                    // do something with entry.Value or entry.Key
                }*/


            data.setInitial(max, min, mid);
        }
    }
}
