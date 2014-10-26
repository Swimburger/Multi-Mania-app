<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:00
 */

namespace Repositories;

use Utilities\Utitilies;

class TalkRepository{

    /**
     * @return array
     */
    public static function getTalks(){
        $sql_query = "SELECT * FROM talk;";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function getTalksWithFavorites($userid){
        $sql_query = "SELECT *, (SELECT COUNT(*) FROM user_talk WHERE user_id = :userid) as isFavorite FROM talk;";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        $stmt->execute(array(':userid'=>$userid));
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function insertTalk($id, $roomId, $isKeynote, $title, $from, $to, $content)
    {
        $sql_query = "INSERT INTO talk (id,title,`from`,`to`,content,room_id,isKeynote) VALUES (:id,:title,:from,:to,:content,:room_id,:isKeynote);";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':title'=>$title,':from'=>$from,':to'=>$to,':content'=>$content,':room_id'=>$roomId,':isKeynote'=>$isKeynote));
    }


}