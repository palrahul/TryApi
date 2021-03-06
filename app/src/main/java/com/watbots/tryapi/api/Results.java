package com.watbots.tryapi.api;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

public final class Results {
  private static final Func1<Result<?>, Boolean> SUCCESSFUL =
          result -> {
            return !result.isError() && result.response().isSuccessful();
          };

  public static Func1<Result<?>, Boolean> isSuccessful() {
    return SUCCESSFUL;
  }

  private Results() {
    throw new AssertionError("No instances.");
  }
}
