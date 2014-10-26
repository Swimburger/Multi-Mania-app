<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:08
 */

namespace Repositories;

use Utilities\Utitilies;

class TagRepository{

    /**
     * @return array
     */
    public static function getTags(){
    $sql_query = "SELECT * FROM tag";
    $con=Utitilies::getConnection();
    $stmt   = $con->query($sql_query);
    return $stmt->fetchAll(\PDO::FETCH_ASSOC);
}

    public static function insertTag($id, $name)
    {
        $sql_query = "INSERT INTO tag VALUES (:id,:name);";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

}