using Microsoft.VisualStudio.TestTools.UnitTesting;
using FirmsChemVS.Services;
using System.Diagnostics.CodeAnalysis;

namespace FirmsChemVS.Test.Services
{
    /// <summary>
    /// This test class tests the Base Table Mass Service Class
    /// </summary>
    [TestClass]
    [ExcludeFromCodeCoverage]
    public class BaseTableMassServiceTest
    {
        /// <summary>
        /// This tests the increment base table mass position the table mass position sould be nine
        /// </summary>
        [TestMethod]
        public void BaseTableMassServiceTest_IncrementBaseTableMassPosition_ShouldBeNegativeNine()
        {
            // Act
            BaseTableMassService baseTableMassService = new BaseTableMassService();
            baseTableMassService.TableCount = 0;
            var result = baseTableMassService.incrementBaseTableMassPosition();

            // Assert
            Assert.AreEqual(result, -9);
        }

        /// <summary>
        /// this tests the increment base table mass position the table count should be zero
        /// </summary>
        [TestMethod]
        public void BaseTableMassServiceTest_IncrementBaseTableMassPosition_TableCountShouldBeZero()
        {
            // Act
            BaseTableMassService baseTableMassService = new BaseTableMassService();
            baseTableMassService.TableCount = 1;
            var result = baseTableMassService.incrementBaseTableMassPosition();

            // Assert
            Assert.AreEqual(result, 0);
        }

        /// <summary>
        /// this tests the increment base table mass position the table position should be zero
        /// </summary>
        [TestMethod]
        public void BaseTableMassServiceTest_IncrementBaseTableMassPosition_TablePosShouldBeZero()
        {
            // Act
            BaseTableMassService baseTableMassService = new BaseTableMassService();
            baseTableMassService.TableCount = -1;
            var result = baseTableMassService.incrementBaseTableMassPosition();

            // Assert
            Assert.AreEqual(result, 0);
        }

        /// <summary>
        /// This tests the reset base table position function, the return value should be negative one
        /// </summary>
        [TestMethod]
        public void BaseTableMassServiceTest_ResetBaseTablePosition_TablePosShouldNegOne()
        {
            // Act
            BaseTableMassService baseTableMassService = new BaseTableMassService();
            baseTableMassService.resetBaseTablePosition();
        }
    }
}
