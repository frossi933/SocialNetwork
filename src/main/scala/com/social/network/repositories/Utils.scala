package com.social.network.repositories

import com.social.network.utils.Sorting

object Utils {

  def sortingFragment(field: String, sorting: Sorting): String = s"order by $field ${sorting.toString}"

  def sortingFragment(mainField: String, secondaryField: String, sorting: Sorting): String = s"order by $mainField ${sorting.toString}, $secondaryField ${sorting.toString}"

}
