using System;
using System.Collections.Generic;
using System.Collections;

namespace FirmsChemVS
{
    public class DataGridViewHelper
    {
        public static Dictionary<string, int> elementDataGridToDictionary(List<ElementGridBinder> elements)
        {
            var elementMap = new Dictionary<string, int>();

            foreach (ElementGridBinder binder in elements)
            {
                if (binder.Count > 0)
                {
                    elementMap.Add(binder.Symbol, binder.Count);
                }
            }

            return elementMap;
        }
    }
}
