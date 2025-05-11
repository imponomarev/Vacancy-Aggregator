package com.example.vacancy_aggregator.service.impl.resume;

import com.example.vacancy_aggregator.data.resume.Resume;
import com.example.vacancy_aggregator.service.ResumeProvider;
import com.example.vacancy_aggregator.service.ResumeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResumeSearchServiceTest {

    @Mock
    private ResumeProvider p1;
    @Mock
    private ResumeProvider p2;

    private ResumeSearchService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ResumeSearchService(List.of(p1, p2));
    }

    private void setAuthorities(String... roles) {
        // Собираем нужный сет ролей
        Set<GrantedAuthority> auths = Stream.of(roles)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toSet());

        // Мокаем Authentication
        Authentication auth = mock(Authentication.class);
        // Вместо when(auth.getAuthorities()).thenReturn(auths);
        doReturn(auths).when(auth).getAuthorities();

        // Мокаем SecurityContext
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }

    @Test
    void whenNotPro_thenAccessDenied() {
        // given no PRO role
        setAuthorities("FREE");

        ResumeQuery query = new ResumeQuery(
                "x", 0, 10, "area", null,
                null, null, null, null,
                null, null, null
        );

        // when / then
        assertThrows(AccessDeniedException.class, () -> service.search(query));
    }

    @Test
    void whenProAndNoFilter_thenAllProvidersQueried() {
        // given PRO user
        setAuthorities("PRO");

        ResumeQuery query = new ResumeQuery(
                "text", 1, 5, "area", null,
                null, null, null, null,
                null, null, null
        );

        Resume r1 = mock(Resume.class);
        Resume r2 = mock(Resume.class);

        when(p1.providerName()).thenReturn("hh");
        when(p1.search(query)).thenReturn(of(r1));

        when(p2.providerName()).thenReturn("sj");
        when(p2.search(query)).thenReturn(of(r2));

        // when
        List<Resume> result = service.search(query);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));

        verify(p1).search(query);
        verify(p2).search(query);
    }

    @Test
    void whenProAndFilterSpecified_thenOnlyThoseProviders() {
        // given PRO user
        setAuthorities("PRO");

        // only "hh" requested
        ResumeQuery query = new ResumeQuery(
                "text", 1, 5, "area", of("hh"),
                null, null, null, null,
                null, null, null
        );

        Resume r1 = mock(Resume.class);

        when(p1.providerName()).thenReturn("hh");
        when(p1.search(query)).thenReturn(of(r1));

        when(p2.providerName()).thenReturn("sj"); // not in filter

        // when
        List<Resume> result = service.search(query);

        // then
        assertEquals(1, result.size());
        assertEquals(r1, result.get(0));

        verify(p1).search(query);
        verify(p2, never()).search(any());
    }

    @Test
    void whenProAndEmptyFilterList_thenAllProviders() {
        // given PRO user
        setAuthorities("PRO");

        // empty providers list = no filtering
        ResumeQuery query = new ResumeQuery(
                "text", 1, 5, "area", of(),
                null, null, null, null,
                null, null, null
        );

        Resume r1 = mock(Resume.class);
        Resume r2 = mock(Resume.class);

        when(p1.providerName()).thenReturn("hh");
        when(p1.search(query)).thenReturn(of(r1));
        when(p2.providerName()).thenReturn("sj");
        when(p2.search(query)).thenReturn(of(r2));

        // when
        List<Resume> result = service.search(query);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
    }
}