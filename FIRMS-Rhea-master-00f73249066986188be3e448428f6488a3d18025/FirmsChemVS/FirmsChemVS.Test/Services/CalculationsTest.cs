using Microsoft.VisualStudio.TestTools.UnitTesting;
using FirmsChemVS.Services;
using System.Diagnostics.CodeAnalysis;
using Microsoft.QualityTools.Testing.Fakes;
using FirmsChemVS.Services.Fakes;

namespace FirmsChemVS.Test.Services
{
    /// <summary>
    /// Summary description for Calculations
    /// </summary>
    [TestClass]
    [ExcludeFromCodeCoverage]
    public class CalculationsTest
    {
        /// <summary>
        /// this test test the calculateAlphaForRow function
        /// </summary>
        [TestMethod]
        public void Calculations_CalculateAlphaForRow_ShouldReturnSevenPointFive()
        {
            using (ShimsContext.Create())
            {
                // Asign
                ShimIsotopeService.InstanceGet = () => new IsotopeService();

                // Act
                var calculation = new Calculations();
                var result = calculation.calculateAlphaForRow(4.0, 30.0);

                // Assert
                Assert.AreEqual(result, 7.5);
            }
        }
    }
}
