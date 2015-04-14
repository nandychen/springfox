/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.swagger2.readers.parameter
import com.google.common.base.Optional
import com.wordnik.swagger.annotations.ApiParam
import org.springframework.core.MethodParameter
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.schema.DefaultGenericTypeNamingStrategy
import springfox.documentation.service.ResolvedMethodParameter
import springfox.documentation.spi.service.contexts.ParameterContext
import springfox.documentation.spring.web.mixins.ModelProviderForServiceSupport
import springfox.documentation.spring.web.mixins.RequestMappingSupport
import springfox.documentation.spring.web.plugins.DocumentationContextSpec

import static com.google.common.base.Optional.*

@Mixin([RequestMappingSupport, ModelProviderForServiceSupport])
class ParameterNameReaderSpec extends DocumentationContextSpec {
  def "param required"() {
    given:
    ResolvedMethodParameter resolvedMethodParameter = Mock(ResolvedMethodParameter)
    def genericNamingStrategy = new DefaultGenericTypeNamingStrategy()
    ParameterContext parameterContext = new ParameterContext(resolvedMethodParameter, new ParameterBuilder(),
        context(), genericNamingStrategy)
    when:
    def sut = nameReader(apiParam)
    sut.apply(parameterContext)
    then:
    parameterContext.parameterBuilder().build().name == expectedName
    where:
    apiParam                                                            | paramType | expectedName
    [name: { -> "bodyParam" }, value: { -> "body Param"}] as ApiParam   | "body"    | "bodyParam"
    null                                                                | "body"    | null
  }

  def nameReader(annotation) {
    new ParameterNameReader() {
      @Override
      def Optional<ApiParam> apiParam(MethodParameter mp) {
        return fromNullable(annotation)
      }
    }
  }
}