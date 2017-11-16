/*
This file.  Yep this file :P
*/
using System;
using System.Collections.Generic;
using System.Linq;

namespace FirmsChemVS.Services
{
    
    public class Abundance
    {
        /// <summary>
        /// This is the constructor function for the Abundance class.
        /// </summary>
        public Abundance()
        {
            //Variables initialisation
            calculated = new List<double>();
            experimental = new List<double>();
            numerators = new List<double>();
            alphas = new List<double>();

            chems = new List<List<int>>();

            mu = new List<double>();

            setSigmaT(0.00);

            xInitial = new List<double>();
            xLower = new List<double>();
            xUpper = new List<double>();

            initialGuess = new List<double>();
            initialMin = new List<double>();
            initialMax = new List<double>();

            iterations = 0;
        }
        //Variables Declaration
        public List<double> calculated;
        public List<double> experimental;
        public List<double> numerators;
        public List<double> alphas;

        public List<double> initialGuess;
        public List<double> initialMin;
        public List<double> initialMax;

        public List<List<int>> chems;

        public List<double> mu;

        private double abundanceTotal;
        public List<double> xInitial;
        public List<double> xLower;
        public List<double> xUpper;

        private double sigmaT;


        private int iterations;

        /// <summary>
        /// Name: incIterations
        /// Purpose:  This function increases the private member variable iterations
        /// by 1.  
        /// This returns nothing.
        /// </summary>
        public void incIterations()
        {
            iterations++;
        }

        /// <summary>
        /// Name: resetIterations
        /// Purpose:  This function simply sets the private member variable
        /// iterations to 0.
        /// This returns nothing.
        /// </summary>
        public void resetIterations()
        {
            iterations = 0;
        }

        /// <summary>
        /// Name: getIterations
        /// Purpose:  The purpose of the function is simply to return the
        /// private member variable iterations.
        /// </summary>
        /// <returns>value of iterations</returns>
        public int getIterations()
        {
            return iterations;
        }

        /// <summary>
        /// Name: setInitial
        /// Purpose:  This function sets the public member lists(why are these public??)
        /// initialMax, initialMin, and initialGuess.
        /// </summary>
        /// <param name="max">a list of doubles (use unknown)</param>
        /// <param name="min">a list of doubles (use unknown)</param>
        /// <param name="mid">a list of doubles (use unknown)</param>
        public void setInitial( List<Double> max,  List<Double>min,  List<Double>mid)
        {
            initialMax = max;
            initialMin = min;
            initialGuess = mid;

        }

        /// <summary>
        /// Name: getXInitial
        /// Purpose: This function returns the value of the xInitial
        /// member variable.
        /// </summary>
        /// <returns>list of initial x values</returns>
        public List<double> getXInitial()
        {
            return xInitial;
        }

        /// <summary>
        /// Name: getXLower
        /// Purpose: This function returns the value of the xLower
        /// member variable.
        /// </summary>
        /// <returns>list of lower x values</returns>
        public List<double> getXLower()
        {
            return xLower;
        }

        /// <summary>
        /// Name: getXUpper
        /// Purpose: This function returns the value of the xUpper
        /// member variable
        /// </summary>
        /// <returns>value of xUpper</returns>
        public List<double> getXUpper()
        {
            return xUpper;
        }

        /// <summary>
        /// Name: countChem
        /// Purpose: This function returns the value of the count
        /// of the items in the chems list (What is chems?)
        /// </summary>
        /// <returns>number of items in chems list</returns>
        public int countChem()
        {
            return chems.Count();
        }

        /// <summary>
        /// Name: total
        /// Purpose: This returns the number of items in the
        /// calculated list
        /// </summary>
        /// <returns>number of items in calculated list</returns>
        public int total()
        {
            return calculated.Count;
        }

        /// <summary>
        /// Name: addCalculated
        /// Puprose:  This function adds a value into the 
        /// calculated list.  
        /// </summary>
        /// <param name="val">the value which needs to be added to the calculated list</param>
        public void addCalculated( double val )
        {
            calculated.Add(val);
        }

        /// <summary>
        /// Name: addMu
        /// Purpose: This functions adds a value to the mu member list
        /// </summary>
        /// <param name="val">value to be added into the mu list</param>
        public void addMu( double val )
        {
            mu.Add(val);
        }

        /// <summary>
        /// Name: addChems
        /// Purpose:  This function adds a list of integers (a chemical compound?)
        /// to the chems list.  
        /// </summary>
        /// <param name="vals">a list to be added to the chems list</param>
        public void addChems(List<int> vals)
        {
            chems.Add(vals);
        }

        /// <summary>
        /// Name: addExperimental
        /// Purpose: this adds values to the experimental list.  It's currently unknown
        /// what these experimentals are but I assume experimental data
        /// </summary>
        /// <param name="val">Value to be added to the experimental list</param>
        public void addExperimental(double val)
        {
            experimental.Add(val);
        }

        /// <summary>
        /// Name: addNumerator
        /// Purpose: This function adds a single value to the numerators list
        /// </summary>
        /// <param name="val">value to be added to list</param>
        public void addNumerator(double val)
        {
            numerators.Add(val);
        }

        /// <summary>
        /// Name: addAlpha
        /// Purpose:  This adds a single value to the alphas list
        /// </summary>
        /// <param name="val">value to be added to alpha list</param>
        public void addAlpha(double val)
        {
            alphas.Add(val);
        }

        /// <summary>
        /// Name: setSigmaT
        /// Purpose:  this sets the sigmaT member variable to the value passed in
        /// </summary>
        /// <param name="val">value to set sigmaT</param>
        public void setSigmaT( double val )
        {
            sigmaT = val;
        }

        /// <summary>
        /// Name: getSigmaT
        /// Purpose: This function returns the value of the sigmaT member variable
        /// </summary>
        /// <returns>value of the SigmaT variable</returns>
        public double getSigmaT()
        {
            return sigmaT;
        }

        /// <summary>
        /// Name: isSigmaTSet
        /// Purpose:  This function checks to see if the SigmaT is positive
        /// </summary>
        /// <returns>false if value is less than or equal to 0 and true if the value is positive</returns>
        public bool isSigmaTSet()
        {
            if (sigmaT <= 0.0)
                return false;
            else
                return true;
        }

        /// <summary>
        /// Name:  inNumerator
        /// Purpose:  This function checks to see if a given index is in a given
        /// index of the chems list of lists.  
        /// </summary>
        /// <param name="xindex">to be checked for</param>
        /// <param name="chemindex">to be checked in</param>
        /// <returns></returns>
        public bool inNumerator( int xindex, int chemindex )
        {
            if (chems[chemindex].Contains(xindex))
                return true;

            else
                return false;
        }

        /// <summary>
        /// Name: numeratorGen
        /// Purpose: Apparently generates a numerator for each mu.
        /// It does this by initializing them all to 1 and then 
        /// multiplying that by setting it equal to the product of each of the elements
        /// in the list that got passed in.
        /// </summary>
        /// <param name="x">A list of double values used to set the numerators</param>
        public void numeratorGen( List<double> x )
        {
            //Generate the numerator for each mu
            //Ben has question.  What's the purpose of this first loop?
            //I mean I know it's so we don't get 0 in the next one but shouldn't
            //(couldn't) this be it's own function initNumerators????
            for (int k = 0; k < this.numerators.Count; k++ )
            {
                this.numerators[k] = 1;
            }

            for (int i = 0; i < this.numerators.Count; i++)
            {

                for (int j = 0; j < x.Count; j++)
                {

                    if (inNumerator(j, i))
                    {
                        this.numerators[i] *= x[j];
                    }

                }
            }
        }

        /// <summary>
        /// Name: setAbundanceTotal
        /// Purpose: this is a setter function for the
        /// abundanceTotal member variable
        /// </summary>
        /// <param name="val">the value which the abundance total needs to be set to</param>
        public void setAbundanceTotal( double val )
        {
            abundanceTotal = val;
        }

        /// <summary>
        /// Name: getAbundanceTotal
        /// Purpose:  This function returns the value of the abundance total member variable.
        /// </summary>
        /// <returns>value in abundanceTotal</returns>
        public double getAbundanceTotal()
        {
            return abundanceTotal;
        }
    }
}
