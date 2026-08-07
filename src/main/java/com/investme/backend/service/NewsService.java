package com.investme.backend.service;

import com.investme.backend.exception.NewsNotFoundException;
import com.investme.backend.exception.NewsViewNotStartedException;
import com.investme.backend.dto.NewsDetailResponse;
import com.investme.backend.dto.NewsListResponse;
import com.investme.backend.entity.News;
import com.investme.backend.entity.User;
import com.investme.backend.entity.UserNewsEvent;
import com.investme.backend.repository.NewsRepository;
import com.investme.backend.repository.UserNewsEventRepository;
import com.investme.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final UserNewsEventRepository userNewsEventRepository;

    @Transactional(readOnly = true)
    public List<NewsListResponse> getNewsList(
            List<Long> excludeNewsIds
    ) {

        List<News> newsList =
                new ArrayList<>(newsRepository.findAll());

        if (excludeNewsIds != null && !excludeNewsIds.isEmpty()) {
            newsList.removeIf(
                    news -> excludeNewsIds.contains(news.getNewsId())
            );
        }

        Collections.shuffle(newsList);

        return newsList.stream()
                .limit(3)
                .map(news -> new NewsListResponse(
                        news.getNewsId(),
                        news.getTitle()
                ))
                .toList();
    }

    @Transactional
    public NewsDetailResponse getNewsDetail(
            Long newsId,
            String loginUserId
    ) {

        News news = newsRepository.findById(newsId)
        .orElseThrow(NewsNotFoundException::new);

        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        UserNewsEvent event =
                new UserNewsEvent(
                        user.getId(),
                        newsId
                );

        userNewsEventRepository.save(event);

        return new NewsDetailResponse(
                news.getNewsId(),
                news.getTitle(),
                news.getContent(),
                news.getPublishedAt()
        );
    }

    @Transactional
    public void completeNewsView(
            Long newsId,
            String loginUserId,
            Integer durationSeconds
    ) {

        if (!newsRepository.existsById(newsId)) {
            throw new NewsNotFoundException();
      }

        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        UserNewsEvent event =
                userNewsEventRepository
                        .findTopByUserIdAndNewsIdAndDurationSecondsIsNullOrderByViewedAtDesc(
                                user.getId(),
                                newsId
                        )
                   .orElseThrow(NewsViewNotStartedException::new);

        event.completeView(durationSeconds);
    }
}
