The AutoLift library is about enhancing the experience of using Scala such that it allows code to be written which arbitrarily nests basic types and use more complex type patterns without having to resort to the nuclear approach of Monad Transformers. Moreover, all attempts have been made to hide the complexities of the implementation as well as any direct dependencies. Users do not have to use or even understand the ideas behind Scalaz to enjoy the benefits.

Several points about this documentation:
 * All code examples are checked at compile time using the [tut SBT Plugin](https://github.com/tpolecat/tut)
 * The main sections are divided into the three concepts: Lifting, Folding and Transforming.
 * It's a living, breathing document so pull requests are appreciated to help make this more approachable.


//TODO:
// 1. the "do i have no know scalaz" section
// 2. How to add it to a project and what imports to pull in
// 3. Page on how to extend your library to work with this