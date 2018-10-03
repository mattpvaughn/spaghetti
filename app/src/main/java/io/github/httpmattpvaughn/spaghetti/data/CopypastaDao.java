package io.github.httpmattpvaughn.spaghetti.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Maybe;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
@Dao
public interface CopypastaDao {
    // In the future, change queries to FST4
    @Query("SELECT * from copypasta where nsfw IN(0, :nsfw) ORDER BY id DESC")
    Maybe<List<Copypasta>> getAll(int nsfw);

    @Query("SELECT * from copypasta where editable = 1 AND (nsfw = :nsfw OR nsfw = 0) ORDER BY id DESC")
    Maybe<List<Copypasta>> getTemplatePosts(int nsfw);

    @Query("SELECT * from copypasta where category != 0 AND (nsfw = :nsfw OR nsfw = 0) ORDER BY id DESC")
    Maybe<List<Copypasta>> getAsciiPosts(int nsfw);

    @Query("SELECT * from copypasta where ((tags like :query) OR (title like :query) OR (content like :query)) AND (nsfw = :nsfw OR nsfw = 0)")
    Maybe<List<Copypasta>> search(String query, int nsfw);

    @Query("SELECT * from copypasta where id = :id")
    Maybe<Copypasta> getCopypastaById(int id);
}
