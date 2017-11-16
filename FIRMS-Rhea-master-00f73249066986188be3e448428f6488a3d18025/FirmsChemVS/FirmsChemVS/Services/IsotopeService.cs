using System;
using System.Collections.Generic;
using System.Linq;
using System.Data;
using FirmsChemVS.Repositories;
using System.Collections;


namespace FirmsChemVS.Services
{
    public class IsotopeService
    {
        //private static System.Collections.GenericOrderByDescending.List<Repositories.Isotope>[] lookUpTableString;
        //private static string[] lookUpTableNumber;
        //private static double[] lookUpTableDict;
        private static List<Repositories.Isotope> lookUpIsotopes;

        private static IsotopeService instance;

        public static IsotopeService Instance
        {

            get
            {
                if (instance == null)
                {
                    instance = new IsotopeService();

                    using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
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


        public List<Repositories.Isotope> getAllIsotopesForAtom(string symbol)
        {

            
            List<Repositories.Isotope> listOfIsotopes = new List<Repositories.Isotope>();

            foreach(Repositories.Isotope curr in lookUpIsotopes)
            {

                if(curr.atomSymbol == symbol)
                {
                    listOfIsotopes.Add(curr);
                }
            }
            listOfIsotopes.OrderByDescending(i => i.abundance);
            /*using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
            {
                var query = ((from isotope in db.Isotopes
                              where isotope.atomSymbol == symbol
                              select isotope).OrderByDescending(i => i.abundance)).ToList();
                listOfIsotopes = query;
            }*/

            return listOfIsotopes;
        }

       

        public string[] getAllIsotopesInList(int[] isotopes)
        {
            string[] listOfIsotopes = new string[isotopes.Count()];
            int pos = 0;

            foreach (int next in isotopes)
            {
                foreach (Repositories.Isotope curr in lookUpIsotopes)
                {
                    if (curr.isotopeNumber == next)
                    {
                        listOfIsotopes[pos] = curr.atomSymbol;
                        pos++;
                    }
                }
            }

            /*using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
            {
                var query = (from isotope in db.Isotopes
                            where isotopes.Contains(isotope.isotopeNumber ?? 0)
                            orderby isotope.isotopeNumber descending
                            select isotope.atomSymbol).ToArray();
                listOfIsotopes = query;
            }*/
            
            return listOfIsotopes;
        }

        public Dictionary<int, double> getAllAbundancesByisotopeNumber(int[] isotopeNumbers)
        {
            Dictionary<int, double> isotopeNumbersWithAbundance = new Dictionary<int, double>();
            
            foreach (int next in isotopeNumbers)
            {
                foreach (Repositories.Isotope curr in lookUpIsotopes)
                {
                    if (curr.isotopeNumber == next)
                    {
                        isotopeNumbersWithAbundance.Add(curr.isotopeNumber.GetValueOrDefault(), curr.abundance.GetValueOrDefault());
                    }
                }
            }

            /*using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
            {
                isotopeNumbersWithAbundance = (from isotope in db.Isotopes
                                               where isotopeNumbers.Contains(isotope.isotopeNumber ?? 0)
                                               select new { isotope.isotopeNumber, isotope.abundance }).ToDictionary(element => (element.isotopeNumber ?? 0), element => (element.abundance ?? 0));
            }*/



            return isotopeNumbersWithAbundance;
        }

        public int[] getAllIsotopesInIsotopeSymbols(string[] atomSymbol)
        {


            int pos = 0;
            foreach (string next in atomSymbol)
            {
                foreach (Repositories.Isotope curr in lookUpIsotopes)
                {
                    if (curr.atomSymbol == next)
                    {
                        //isotopes[pos] = curr.isotopeNumber.GetValueOrDefault();
                        pos++;
                    }
                }
            }

            int[] isotopes = new int[pos];
            pos = 0;

            foreach (string next in atomSymbol)
            {
                foreach (Repositories.Isotope curr in lookUpIsotopes)
                {
                    if (curr.atomSymbol == next)
                    {
                        isotopes[pos] = curr.isotopeNumber.GetValueOrDefault();
                        pos++;
                    }
                }
            }


            /*using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
            {
                isotopes = (from isotope in db.Isotopes
                            where atomSymbol.Contains(isotope.atomSymbol)
                            orderby isotope.isotopeNumber descending
                            select isotope.isotopeNumber ?? 0).ToArray();
            }*/
            return isotopes;
        }


        public double CalculateMolecularWeight(DataTable elements)
        {
            double molecularWeight = 0;

            foreach (DataRow row in elements.Rows)
            {
                if (row[1] as string == "0")
                {
                    continue;
                }
                string symbol = row[0] as string;
                
                if (Convert.ToInt32(row[1]) > 0)
                {
                    int count = 0;
                    List<Repositories.Isotope> tempTable = new List<Repositories.Isotope>();

                    foreach (Repositories.Isotope curr in lookUpIsotopes)
                    {
                        if (curr.atomSymbol == symbol)
                        {
                            
                            count++;
                            tempTable.Add(curr);
                            //molecularWeight += (double)curr.atomMass * Convert.ToInt32(row[1]);
                        }
                    }

                    tempTable.OrderByDescending(i => i.abundance);
                    molecularWeight += (double)tempTable.First().atomMass * Convert.ToInt32(row[1]);
                }
            }

            /*using (FirmsChemVS.Repositories.FirmsChemDBEntities db = new Repositories.FirmsChemDBEntities())
            {
                foreach (DataRow row in elements.Rows)
                {
                    if (row[1] as string == "0")
                    {
                        continue;
                    }
                    string symbol = row[0] as string;
                    if (Convert.ToInt32(row[1]) > 0)
                    {
                        var query = ((from isotope in db.Isotopes
                                      where isotope.atomSymbol == symbol
                                      select isotope).OrderByDescending(i => i.abundance)).First();
                        molecularWeight += (double)query.atomMass * Convert.ToInt32(row[1]);
                    }
                }

            }*/
            return molecularWeight;
        }

        public int CalculateIonMass(Dictionary<string, int> elements)
        {
            int isotopeWeight = 0;

            foreach (string symbol in elements.Keys)
            {
                int count = 0;
                List<Repositories.Isotope> tempTable = new List<Repositories.Isotope>();

                foreach (Repositories.Isotope curr in lookUpIsotopes)
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
                foreach (Repositories.Isotope curr in lookUpIsotopes)
                    if (entry.Key.Equals(curr.atomSymbol))
                        pos++;
                isotopes = new double[pos];
                pos = 0;
                foreach (Repositories.Isotope curr in lookUpIsotopes)
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
