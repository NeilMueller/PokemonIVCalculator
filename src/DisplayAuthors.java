// Fig. 24.23: DisplayAuthors.java
// Displaying the contents of the authors table.

import java.sql.*;

public class DisplayAuthors 
{
   public static void main(String args[])
   {
      final String databaseUrl = "jdbc:derby:C:\\Users\\User\\IdeaProjects\\IVChecker\\lib\\pokedex";
      String pokemonQuery = "SELECT name FROM pokemon WHERE pokemon.id = 3";

      try(
              Connection connection = DriverManager.getConnection(databaseUrl);
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery(pokemonQuery);)
      {
         while (resultSet.next())
         {
            for (int i = 1; i <= 1; i++)
               System.out.printf("%-8s\t", resultSet.getObject(i));
            System.out.println();
         }
      } catch (SQLException sqlException){
         sqlException.printStackTrace();
      }

   } 
} // end class DisplayAuthors



/**************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/

 