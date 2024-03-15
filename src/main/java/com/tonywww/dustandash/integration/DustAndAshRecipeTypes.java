package com.tonywww.dustandash.integration;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import mezz.jei.api.recipe.RecipeType;

public class DustAndAshRecipeTypes {

    public static final RecipeType<IntegratedBlockRecipe> INTEGRATE = RecipeType.create(DustAndAsh.MOD_ID, "integrate", IntegratedBlockRecipe.class);
    public static final RecipeType<MillingMachineRecipe> MILLING = RecipeType.create(DustAndAsh.MOD_ID, "milling", MillingMachineRecipe.class);
    public static final RecipeType<CentrifugeRecipe> CENTRIFUGE = RecipeType.create(DustAndAsh.MOD_ID, "centrifuge", CentrifugeRecipe.class);
    public static final RecipeType<IonizerRecipe> IONIZER = RecipeType.create(DustAndAsh.MOD_ID, "ionizer", IonizerRecipe.class);

}
