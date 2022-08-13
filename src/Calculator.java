import java.sql.*;
import java.util.Locale;

public class Calculator
{

   String databaseUrl;

   private int[] baseStats, ivs, evs, out;
   private double[] natureMod;

   private String[] strings;
   private String bonusStat, weakenedStat;
   private int bonusIndex, weakenedIndex;
   private double prob;

   private int level;
   private String nature;
   private String name;
   private int id;


   public Calculator(){
      databaseUrl = "jdbc:derby:C:\\Users\\User\\IdeaProjects\\DBTest\\lib\\pokedex";
      baseStats = new int[6];
      ivs = new int[6];
      evs = new int[6];
      natureMod = new double[6];
      out = new int[6];
      strings = new String[3];
   }

   public void calculate(){

      final String pokemonIDQuery = "SELECT * FROM pokemon WHERE pokemon.id = " + id;
      final String natureQuery = "SELECT * FROM natures WHERE lower(natures.name) = '" + nature.toLowerCase() + "'";
      final String pokemonNameQuery = "SELECT * FROM pokemon WHERE lower(pokemon.name) = '" + name.toLowerCase() + "'";

      //ensure there is a nature provided before you try to calculate
      if(natureQuery(databaseUrl, natureQuery)) {
         //use name if it is provided, else use ID
         if (name.isEmpty()) {
            pokemonIDQuery(databaseUrl, pokemonIDQuery);
         } else {
            pokemonNameQuery(databaseUrl, pokemonNameQuery);
         }
      }

   }

   /**
    * Queries the pokemon table using the name
    * resultset is of form: int ID, varchar() name, varchar() ideal stat, varchar() worst stat
    *                       int baseHP, int baseAtk, int baseDef, int baseSpAtk, int baseSpDef, int base Spd
    *
    * @param databaseUrl
    * @param pokemonNameQuery
    */
   private void pokemonNameQuery(String databaseUrl, String pokemonNameQuery) {
      try(
              Connection connection = DriverManager.getConnection(databaseUrl);
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery(pokemonNameQuery);)
      {

         if(!resultSet.next()) {
            AlertBox.display("Pokemon " + name +" not found.");
            for (int i = 0; i < 6; i++){
               out[i] = 0;
            }
         } else {
            setValues(resultSet);
         }


      } catch (SQLException sqlException){
         sqlException.printStackTrace();
      }
   }

   /**
    * Queries the nature table using the given nature
    * Result set is of form: Varchar() name, Varchar() bonusStat, varchar() weakenedStat
    * Updates nature mod int array for stat calculation using nature
    *
    * @param databaseUrl
    * @param natureQuery
    * @return
    */

   private boolean natureQuery(String databaseUrl, String natureQuery) {
      try(
              Connection connection = DriverManager.getConnection(databaseUrl);
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery(natureQuery);)
      {

         bonusStat = "";
         weakenedStat = "";

         for(int i = 0; i < 6; i ++){
            natureMod[i] = 1;
         }

         if(!resultSet.next()) {
            AlertBox.display("INVALID NATURE");
            for (int i = 0; i < 6; i++){
               out[i] = 0;
            }
            return false;
         }
         else {

            bonusStat = resultSet.getString(2);
            weakenedStat = resultSet.getString(3);


            switch (bonusStat) {

               case "atk":
                  natureMod[1] = 1.1;
                  bonusIndex = 1;
                  break;
               case "def":
                  natureMod[2] = 1.1;
                  bonusIndex = 2;
                  break;
               case "spAtk":
                  natureMod[3] = 1.1;
                  bonusIndex = 3;
                  break;
               case "spDef":
                  natureMod[4] = 1.1;
                  bonusIndex = 4;
                  break;
               case "spd":
                  natureMod[5] = 1.1;
                  bonusIndex = 5;
                  break;
               case "none":
                  bonusIndex = -1;

            }

            switch (weakenedStat) {

               case "atk":
                  natureMod[1] = 0.9;
                  weakenedIndex = 1;
                  break;
               case "def":
                  natureMod[2] = 0.9;
                  weakenedIndex = 2;
                  break;
               case "spAtk":
                  natureMod[3] = 0.9;
                  weakenedIndex = 3;
                  break;
               case "spDef":
                  natureMod[4] = 0.9;
                  weakenedIndex = 4;
                  break;
               case "spd":
                  natureMod[5] = 0.9;
                  weakenedIndex = 5;
                  break;
               case "none":
                  weakenedIndex = -1;

            }
         }

         return true;

      } catch (SQLException sqlException){
         sqlException.printStackTrace();
         return false;
      }
   }


   /**
    * Queries the pokemon table using the ID
    * resultset is of form: int ID, varchar() name, varchar() ideal stat, varchar() worst stat
    *                       int baseHP, int baseAtk, int baseDef, int baseSpAtk, int baseSpDef, int base Spd
    * @param databaseUrl
    * @param pokemonIDQuery
    */
   private void pokemonIDQuery(String databaseUrl, String pokemonIDQuery) {
      try(
              Connection connection = DriverManager.getConnection(databaseUrl);
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery(pokemonIDQuery);)
      {

         if(!resultSet.next()) {
            AlertBox.display("Pokemon with id" + id + " not found");
            for (int i = 0; i < 6; i++){
               out[i] = 0;
            }
         } else {
            setValues(resultSet);
         }


      } catch (SQLException sqlException){
         sqlException.printStackTrace();
      }
   }

   /**
    * Updates all info so it can be grabbed by the controller for display
    *
    * @param resultSet
    * @throws SQLException
    */
   private void setValues(ResultSet resultSet) throws SQLException {
      id = resultSet.getInt(1);
      strings[0] = resultSet.getString(2);
      strings[1] = resultSet.getString(3);
      strings[2] = resultSet.getString(4);

      for (int i = 0; i < 6; i++) {
         baseStats[i] = resultSet.getInt(i + 5);
      }

      //HP has different Calculation
      out[0] = (int) Math.floor(((baseStats[0] * 2) + ivs[0] + (evs[0] / 4)) * (level / 100) + 10 + level);

      for (int i = 1; i < 6; i++) {
         out[i] = (int) Math.floor((((baseStats[i] * 2) + ivs[i] + (evs[i] / 4)) * level / 100 + 5) * natureMod[i]);
      }

      prob = calcProb();
   }

   /**
    * calculates the "probability" of catching a given pokemon
    * Technically it is not probability because I added a weight to the ideal stat and worse stat,
    * as well as a weight to certain natures.
    * this is because while the likelyhood of any pokemon receiving a given nature is the same
    * however, it is favorable to get a nature that increases the ideal stat of the pokemon
    *
    * @return
    */

   public double calcProb(){
      double prob = 1;

      //Calculate the prob of getting the IVS
      for (int i = 0; i < 6; i ++){
         if(i != weakenedIndex){
            if(i == bonusIndex){
               prob *= (double) (32 - ivs[i]) / 32;
               prob *= (double) (32 - ivs[i]) / 32;
            } else {
               prob *= (double) (32 - ivs[i]) / 32;
            }
         }

      }

      //if nature is a good match
      if(bonusStat.compareTo(strings[1]) == 0){
         prob *= (double) 1/5;
         if(weakenedStat.compareTo(strings[2]) == 0){
            prob *= (double)  1/5;
         }
      }
      return prob;
   }

   public int getId() {
      return id;
   }

   /**
    * I used this method for quick modification of the data base that only required a small line
    * and not a whole sql script. Like when I find spelling error.
    *
    * @return
    */

   private void modificationQuery(String databaseUrl, String modificationQuery) {
      try(
              Connection connection = DriverManager.getConnection(databaseUrl);
              Statement statement = connection.createStatement();
              )
      {

         statement.execute(modificationQuery);

      } catch (SQLException sqlException){
         sqlException.printStackTrace();
      }
   }

   public String[] getStrings() {
      return strings;
   }

   public int[] getOut() {
      return out;
   }

   public double getProb(){
      return prob;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setIvs(int[] ivs) {
      this.ivs = ivs;
   }

   public void setEvs(int[] evs) {
      this.evs = evs;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setNature(String nature) {
      this.nature = nature;
   }

}