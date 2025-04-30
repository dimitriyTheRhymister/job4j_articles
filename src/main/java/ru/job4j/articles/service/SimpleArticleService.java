package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.util.stream.IntStream;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int articleCount, Store<Article> articleStore) {
        LOGGER.info("Геренация статей в количестве {}", articleCount);
        var words = wordStore.findAll();
        IntStream.iterate(0, i -> i < articleCount, i -> i + 1)
                .peek(this::logProgress)
                .mapToObj((x) -> articleGenerator.generate(words))
                .forEach(articleStore::save);
        LOGGER.info("Генерация статей завершена. Всего сгенерировано статей: {}", articleCount);
    }

    private void logProgress(int i) {
        if (i % 10000 == 0) {
            LOGGER.info("Сгенерировано статей: {}", i);
        }
    }
}
