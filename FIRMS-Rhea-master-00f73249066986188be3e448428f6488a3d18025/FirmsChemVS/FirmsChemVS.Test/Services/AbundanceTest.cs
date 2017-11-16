using FirmsChemVS.Services;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;

namespace FirmsChemVS.Test.Services
{
    [TestClass]
    [ExcludeFromCodeCoverage]
    public class AbundanceTest
    {
        /// <summary>
        /// this tests the in numerator function, it should return false
        /// </summary>
        [TestMethod]
        public void Abundance_inNumerator_ShouldBeFalse()
        {
            // Assign
            var listOfInts = new List<List<int>>
            {
                new List<int> { 1, 2, 3 },
                new List<int> { 4, 5, 6 }
            };

            // Act
            Abundance abundance = new Abundance();
            abundance.chems = listOfInts;
            var result = abundance.inNumerator(7, 0);

            // Assert
            Assert.AreEqual(result, false);
        }

        /// <summary>
        /// this tests the in numerator function, should return false
        /// </summary>
        [TestMethod]
        public void Abundance_inNumerator_ShouldBeTrue()
        {
            // Assign
            var listOfInts = new List<List<int>>
            {
                new List<int> { 1, 2, 3 },
                new List<int> { 4, 5, 6 }
            };

            // Act
            Abundance abundance = new Abundance();
            abundance.chems = listOfInts;
            var result = abundance.inNumerator(5, 1);

            // Assert
            Assert.AreEqual(result, true);
        }

        /// <summary>
        /// this tests the is sigma t set function, it should return false
        /// </summary>
        [TestMethod]
        public void Abundance_isSigmaTSet_ShouldBeFalse()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.setSigmaT(-1.0);
            var result = abundance.isSigmaTSet();

            // Assert
            Assert.AreEqual(result, false);
        }

        /// <summary>
        ///  this tests the is sigma t set function, this should return true
        /// </summary>
        [TestMethod]
        public void Abundance_isSigmaTSet_ShouldBeTrue()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.setSigmaT(5.0);
            var result = abundance.isSigmaTSet();

            // Assert
            Assert.AreEqual(result, true);
        }
        
        /// <summary>
        /// this tests the add calulated function, the count should be one and 
        /// that one value should be 5.6
        /// </summary>
        [TestMethod]
        public void Abundance_addCalculated_CountShouldBeOne_ValueShouldBeFivePointSix()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.addCalculated(5.6);

            // Assert
            Assert.AreEqual(abundance.calculated.Count, 1);
            Assert.AreEqual(abundance.calculated[0], 5.6);
        }

        /// <summary>
        /// this tess the add mu function, the count should be one and 
        /// that one value should be 1.9
        /// </summary>
        [TestMethod]
        public void Abundance_addMu_CountShouldBeOne_ValueShouldBeOnePointNine()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.addMu(1.9);

            // Assert
            Assert.AreEqual(abundance.mu.Count, 1);
            Assert.AreEqual(abundance.mu[0], 1.9);
        }

        /// <summary>
        /// this tests the add chems function, the count should be one and 
        /// the value should return a list with values between three and six
        /// </summary>
        [TestMethod]
        public void Abundance_addChems_CountShouldBeOne_ValueShouldBeListOfThreeThroughSix()
        {
            // Assign
            var listOfInts = new List<int> { 3, 4, 5, 6 };

            // Act
            Abundance abundance = new Abundance();
            abundance.addChems(listOfInts);

            // Assert
            Assert.AreEqual(abundance.chems.Count, 1);
            Assert.AreEqual(abundance.chems[0], listOfInts);
        }

        /// <summary>
        /// this tests the add experimental function, the count should be one and
        /// the value should be 3.4
        /// </summary>
        [TestMethod]
        public void Abundance_addExperimental_CountShouldBeOne_ValueShouldBeThreePointFour()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.addExperimental(3.4);

            // Assert
            Assert.AreEqual(abundance.experimental.Count, 1);
            Assert.AreEqual(abundance.experimental[0], 3.4);
        }

        /// <summary>
        /// this tests the add numerator function, the count should be one and
        /// the value should be 2.5
        /// </summary>
        [TestMethod]
        public void Abundance_addNumerator_CountShouldBeOne_ValueShouldBeTwoPointFive()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.addNumerator(2.5);

            // Assert
            Assert.AreEqual(abundance.numerators.Count, 1);
            Assert.AreEqual(abundance.numerators[0], 2.5);
        }

        /// <summary>
        /// this tests the add alpha function, the count should be one and 
        /// the value should be 12.7
        /// </summary>
        [TestMethod]
        public void Abundance_addAlpha_CountShouldBeOne_ValueShouldBeTwelvePointSeven()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.addAlpha(12.7);

            // Assert
            Assert.AreEqual(abundance.alphas.Count, 1);
            Assert.AreEqual(abundance.alphas[0], 12.7);
        }

        /// <summary>
        /// this tests the set abundance total and the get abundance total
        /// the total should be three
        /// </summary>
        [TestMethod]
        public void Abundance_setAbundanceTotal_getAbundanceTotal_ShouldBeThree()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.setAbundanceTotal(3.0);

            // Assert
            Assert.AreEqual(abundance.getAbundanceTotal(), 3.0);
        }

        /// <summary>
        /// this tests the get iterations function, this should be zero
        /// </summary>
        [TestMethod]
        public void Abundance_GetIterations_ShouldBeZero()
        {
            // Act
            Abundance abundance = new Abundance();
            var result = abundance.getIterations();

            // Assert
            Assert.AreEqual(result, 0);
        }

        /// <summary>
        /// this tests the inc iterations function, which should return one
        /// </summary>
        [TestMethod]
        public void Abundance_IncIterations_ShouldBeOne()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.incIterations();
            var result = abundance.getIterations();

            // Assert
            Assert.AreEqual(result, 1);
        }

        /// <summary>
        /// this tests the reset iterations function, which should return zero
        /// </summary>
        [TestMethod]
        public void Abundance_ResetIterations_ShouldBeZero()
        {
            // Act
            Abundance abundance = new Abundance();
            abundance.resetIterations();
            var result = abundance.getIterations();

            // Assert
            Assert.AreEqual(result, 0);
        }

        /// <summary>
        /// this tests the set initial function, the max should be 3, the min
        /// should be 4 and the mid should be 5
        /// </summary>
        [TestMethod]
        public void Abundance_SetIntial_ShouldBeXis3Yis4Zis5()
        {
            // Assign
            List<double> max = new List<double> { 3 };
            List<double> min = new List<double> { 4 };
            List<double> mid = new List<double> { 5 };

            // Act
            Abundance abundance = new Abundance();
            abundance.setInitial(max, min, mid);

            // Assert
            Assert.AreEqual(abundance.initialMax, max);
            Assert.AreEqual(abundance.initialMin, min);
            Assert.AreEqual(abundance.initialGuess, mid);
        }

        /// <summary>
        /// this tests the get initial x function, which should be 3
        /// </summary>
        [TestMethod]
        public void Abundance_GetXIntial_ShouldBe3()
        {
            // Assign
            List<double> x = new List<double> { 3 };

            // Act
            Abundance abundance = new Abundance();
            abundance.xInitial = x;
            var result = abundance.getXInitial();

            // Assert
            Assert.AreEqual(result, x);
        }

        /// <summary>
        ///  this tests the get x lower function, which should be three
        /// </summary>
        [TestMethod]
        public void Abundance_GetXLower_ShouldBe3()
        {
            // Assign
            List<double> x = new List<double> { 3 };

            // Act
            Abundance abundance = new Abundance();
            abundance.xLower = x;
            var result = abundance.getXLower();

            // Assert
            Assert.AreEqual(result, x);
        }

        /// <summary>
        /// this tests the get x upper function, which should be three
        /// </summary>
        [TestMethod]
        public void Abundance_GetXUpper_ShouldBe3()
        {
            // Assign
            List<double> x = new List<double> { 3 };

            // Act
            Abundance abundance = new Abundance();
            abundance.xUpper = x;
            var result = abundance.getXUpper();

            // Assert
            Assert.AreEqual(result, x);
        }

        /// <summary>
        /// this tests the count chem function, which should return three
        /// </summary>
        [TestMethod]
        public void Abundance_countChem_ShouldBe3()
        {
            // Assign
            List<List<int>> x = new List<List<int>> { new List<int> { 3 }, new List<int> { 4 }, new List<int> { 5 } };

            // Act
            Abundance abundance = new Abundance();
            abundance.chems = x;
            var result = abundance.countChem();

            // Assert
            Assert.AreEqual(result, 3);
        }

        /// <summary>
        /// this tests the total function, which should return three
        /// </summary>
        [TestMethod]
        public void Abundance_Total_ShouldBe3()
        {
            // Assign
            List<double> x = new List<double> { 3, 4, 5 };

            // Act
            Abundance abundance = new Abundance();
            abundance.calculated = x;
            var result = abundance.total();

            // Assert
            Assert.AreEqual(result, 3);
        }

        /// <summary>
        /// this tests the get sigma t function, which should be zero
        /// </summary>
        [TestMethod]
        public void Abundance_GetSigmaT_ShouldBeZero()
        {
            // Act
            Abundance abundance = new Abundance();
            var result = abundance.getSigmaT();

            // Assert
            Assert.AreEqual(result, 0);
        }
    }
}
