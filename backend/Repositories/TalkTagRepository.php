<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utitilies;

class TalkTagRepository {
    /**
     * @return array
     */
    public static function  getTalkTags()
    {
        $sql_query = "SELECT * FROM talk_tag";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function insertTalkTag($talkId, $tagId)
    {
        $sql_query = "INSERT INTO talk_tag (talk_id, tag_id) VALUES (:talk_id,:tag_id);";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':talk_id'=>$talkId,':tag_id'=>$tagId));
    }
} 