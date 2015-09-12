# EvolutionaryAgent

## how to apply parallel Evaluation

You should do the following steps in order to apply parallel evaluation to your optimization algorithm:

1. Download the [`optimization.jar`](https://github.com/camilourd/EvolutionaryAgent/raw/master/lib/optimization.jar) file, which is in the folder lib.
2. Add the previous file to your project libraries.
3. Substitute `Solution.evaluate((Vector)pop, function);` with `Solution.paralelEvaluate((Vector)pop, function, numberOfThreads);` in the line where the first population is being evaluated.
4. Add the line `transformation.activateParalelEvaluation(numberOfThreads);` after the Transformation class have been defined, for example: `Transformation transformation = new HAEA(haeaOperators, grow, selection);`.

Finally, if you do these four steps, your algorithm ought to be evaliating the fitness function in parallel form.
