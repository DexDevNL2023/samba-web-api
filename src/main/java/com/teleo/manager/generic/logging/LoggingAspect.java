package com.teleo.manager.generic.logging;

import com.teleo.manager.generic.utils.AppConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Aspect de la journalisation de l'exécution des composants Spring du service et du référentiel.
 * Par défaut, il ne fonctionne qu'avec le profil "dev".
 */
@Aspect
public class LoggingAspect {

    private final Environment env;

    public LoggingAspect(Environment env) {
        this.env = env;
    }

    /**
     * Pointcut qui correspond à tous les référentiels, services et points de terminaison Web REST.
     */
    @Pointcut(
            "within(@org.springframework.stereotype.Repository *)" +
                    " || within(@org.springframework.stereotype.Service *)" +
                    " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {
        // La méthode est vide car il ne s'agit que d'un Pointcut, les implémentations sont dans les conseils.
    }

    /**
     * Pointcut qui correspond à tous les beans Spring dans les principaux packages de l'application.
     */
    @Pointcut(
            "within(com.teleo.manager.authentification.repositories..*)" +
                    " || within(com.teleo.manager.authentification.services..*)" +
                    " || within(com.teleo.manager.authentification.controllers..*)"
    )
    public void applicationPackagePointcut() {
        // La méthode est vide car il ne s'agit que d'un Pointcut, les implémentations sont dans les conseils.
    }

    /**
     * Récupère le {@link Logger} associé au {@link JoinPoint} donné.
     *
     * @param joinPoint point de jointure pour lequel nous voulons l'enregistreur.
     * @return {@link Logger} associé au {@link JoinPoint} donné.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Methode qui enregistrent les méthodes lançant des exceptions.
     *
     * @param joinPoint rejoint le point pour obtenir des conseils.
     * @param e         exception.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Profiles.of(AppConstants.SPRING_PROFILE_DEVELOPMENT))) {
            logger(joinPoint)
                    .error(
                            "Exception dans {}() avec cause = '{}' et exception = '{}'",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL",
                            e.getMessage(),
                            e
                    );
        } else {
            logger(joinPoint)
                    .error(
                            "Exception dans {}() avec cause = {}",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL"
                    );
        }
    }

    /**
     * Methode qui enregistrent l'entrée et la sortie d'une méthode.
     *
     * @param joinPoint rejoint le point pour obtenir des conseils.
     * @return résultat.
     * @throws Throwable lance {@link IllegalArgumentException}.
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Entrez : {}() avec argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Sortie : {}() avec résultat = {}", joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Argument illégal : {} dans {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @Before("@annotation(LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        // Log method entry for controllers
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String parameters = (String) ((Stream) Arrays.stream(args).sequential()).map(Object::toString).collect(Collectors.joining());
        logger(joinPoint).info("Méthode de saisie : {}.{} avec paramètres : {}", className, methodName, parameters);
    }

    @After("@annotation(LogExecution)")
    public void logAfter(JoinPoint joinPoint) {
        // Log method exit for controllers
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger(joinPoint).info("Méthode de sortie : " + className + "." + methodName);
    }
}
