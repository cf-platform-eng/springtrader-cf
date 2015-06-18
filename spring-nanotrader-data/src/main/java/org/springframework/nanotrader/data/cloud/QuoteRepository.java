package org.springframework.nanotrader.data.cloud;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.springframework.data.domain.PageRequest;
import org.springframework.nanotrader.data.domain.Quote;
import org.springframework.stereotype.Repository;

import feign.Headers;
import feign.RequestLine;

@Repository
public interface QuoteRepository {

	@RequestLine("GET /findBySymbol/{symbol}")
	public Quote findBySymbol(@Named("symbol") String symbol);

	@RequestLine("GET /findById/{id}")
	public Quote findQuote(@Named("id") Integer id);

	@RequestLine("GET /findAll")
	public List<Quote> findAll();

	@RequestLine("GET /findBySymbolIn?symbols={symbols}")
	public List<Quote> findBySymbolIn(@Named("symbols") Set<String> symbols);

	@RequestLine("GET /findAllPaged?page={from}&size={to}")
	public List<Quote> findQuoteEntries(@Named("from") int from,
			@Named("to") int to);

	@RequestLine("GET /findAllPaged?page={from}&size={to}&sort={column},{direction}")
	public List<Quote> findQuoteEntries(@Named("page") PageRequest page);

	@RequestLine("GET /count")
	public Integer countAllQuotes();

	@RequestLine("GET /marketSummary")
	public Map<String, Long> marketSummary();
	
	@RequestLine("GET /topLosers?page=0&size=3")
	public List<Quote> topLosers();
	
	@RequestLine("GET /topGainers?page=0&size=3")
	public List<Quote> topGainers();
	
	@RequestLine("POST /save")
	@Headers("Content-Type: application/json")
	public Quote save(Quote quote);
	
	@RequestLine("POST /delete")
	@Headers("Content-Type: application/json")
	public void delete(Quote quote);
}