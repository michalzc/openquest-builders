package michalz.openquest.tools.cats.effect.specs2

import cats.effect.testing.UnsafeRun
import org.specs2.execute.AsResult
import org.specs2.specification.core.{AsExecution, Execution}

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}

trait CateEffect:

  protected val Timeout: Duration                     = 10.seconds
  protected def finiteTimeout: Option[FiniteDuration] =
    Some(Timeout) collect { case fd: FiniteDuration =>
      fd
    }

  implicit def effectAsExecution[F[_]: UnsafeRun, R](implicit R: AsResult[R]): AsExecution[F[R]] =
    new AsExecution[F[R]]:
      override def execute(t: => F[R]): Execution =
        Execution
          .withEnvAsync(_ => UnsafeRun[F].unsafeToFuture(t, finiteTimeout))
          .copy(timeout = finiteTimeout)
