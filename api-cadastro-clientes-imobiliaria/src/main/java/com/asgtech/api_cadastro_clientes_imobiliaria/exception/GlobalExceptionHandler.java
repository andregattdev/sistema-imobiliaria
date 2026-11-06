package com.asgtech.api_cadastro_clientes_imobiliaria.exception;

import java.net.URI;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ProblemDetail> handleRequisicaoInvalida(
            RequisicaoInvalidaException exception,
            HttpServletRequest request) {
        return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(ServicoExternoException.class)
    public ResponseEntity<ProblemDetail> handleServicoExterno(
            ServicoExternoException exception,
            HttpServletRequest request) {
        return criarResposta(HttpStatus.BAD_GATEWAY, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleArgumentoNaoValidado(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        List<String> erros = exception.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .distinct()
                .toList();

        ProblemDetail problem = criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos.",
                request);
        problem.setProperty("erros", erros);
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleViolacaoDeRestricao(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        List<String> erros = exception.getConstraintViolations().stream()
                .map(violacao -> violacao.getPropertyPath() + ": " + violacao.getMessage())
                .sorted()
                .toList();

        ProblemDetail problem = criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais parâmetros são inválidos.",
                request);
        problem.setProperty("erros", erros);
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleCorpoNaoLegivel(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "O corpo da requisição está inválido ou malformado.",
                request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleViolacaoDeIntegridade(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        return criarResposta(
                HttpStatus.CONFLICT,
                "A operação conflita com os dados existentes.",
                request);
    }

    private ResponseEntity<ProblemDetail> criarResposta(
            HttpStatus status,
            String detalhe,
            HttpServletRequest request) {
        return ResponseEntity.status(status).body(criarProblemDetail(status, detalhe, request));
    }

    private ProblemDetail criarProblemDetail(
            HttpStatus status,
            String detalhe,
            HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detalhe);
        problem.setTitle(status.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}
