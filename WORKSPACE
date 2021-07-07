load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# golang rules
http_archive(
    name = "io_bazel_rules_go",
    sha256 = "69de5c704a05ff37862f7e0f5534d4f479418afc21806c887db544a316f3cb6b",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_go/releases/download/v0.27.0/rules_go-v0.27.0.tar.gz",
        "https://github.com/bazelbuild/rules_go/releases/download/v0.27.0/rules_go-v0.27.0.tar.gz",
    ],
)

http_archive(
    name = "bazel_gazelle",
    sha256 = "62ca106be173579c0a167deb23358fdfe71ffa1e4cfdddf5582af26520f1c66f",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-gazelle/releases/download/v0.23.0/bazel-gazelle-v0.23.0.tar.gz",
        "https://github.com/bazelbuild/bazel-gazelle/releases/download/v0.23.0/bazel-gazelle-v0.23.0.tar.gz",
    ],
)

load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")
load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies")

go_rules_dependencies()

go_register_toolchains(version = "1.16")

gazelle_dependencies()

# grpc rules
http_archive(
    name = "rules_proto_grpc",
    sha256 = "7954abbb6898830cd10ac9714fbcacf092299fda00ed2baf781172f545120419",
    strip_prefix = "rules_proto_grpc-3.1.1",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/3.1.1.tar.gz"],
)

load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")

rules_proto_grpc_toolchains()

rules_proto_grpc_repos()

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

load("@rules_proto_grpc//go:repositories.bzl", rules_proto_grpc_go_repos = "go_repos")

rules_proto_grpc_go_repos()

load("@rules_proto_grpc//cpp:repositories.bzl", rules_proto_grpc_cpp_repos = "cpp_repos")

rules_proto_grpc_cpp_repos()

load("@com_github_grpc_grpc//bazel:grpc_deps.bzl", "grpc_deps")

grpc_deps()

## add the external repositories for the Java toolchain

RULES_JVM_EXTERNAL_TAG = "4.0"

RULES_JVM_EXTERNAL_SHA = "31701ad93dbfe544d597dbe62c9a1fdd76d81d8a9150c2bf1ecf928ecdf97169"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_proto_grpc//java:repositories.bzl", rules_proto_grpc_java_repos = "java_repos")

rules_proto_grpc_java_repos()

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "grpc_java_repositories")

maven_install(
    artifacts = [
        "junit:junit:4.12",
        "org.assertj:assertj-core:3.15.0",
        "io.grpc:grpc-testing:1.32.1",
        "org.mockito:mockito-core:2.28.2",
        "org.springframework.boot:spring-boot-autoconfigure:2.5.2",
        "org.springframework.boot:spring-boot:2.5.2",
        "org.springframework.boot:spring-boot-starter-web:2.5.2",
        "org.springframework:spring-context:5.3.8",
        "org.springframework:spring-web:5.3.8",
    ] + IO_GRPC_GRPC_JAVA_ARTIFACTS,
    generate_compat_repositories = True,
    override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
    repositories = [
        "https://maven.aliyun.com/repository/central",
        "https://maven.aliyun.com/repository/spring/",
    ],
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()

grpc_java_repositories()

## add the external repositories for the Rust toolchain
http_archive(
    name = "rules_rust",
    sha256 = "18c0a02a007cd26c3f5b4d21dc26a80af776ef755460028a796bc61c649fdf3f",
    strip_prefix = "rules_rust-467a301fd665db344803c1d8a2401ec2bf8c74ce",
    urls = [
        # Master branch as of 2021-04-23
        "https://github.com/bazelbuild/rules_rust/archive/467a301fd665db344803c1d8a2401ec2bf8c74ce.tar.gz",
    ],
)

load("@rules_proto_grpc//rust:repositories.bzl", rules_proto_grpc_rust_repos = "rust_repos")

rules_proto_grpc_rust_repos()

grpc_deps()

load("@rules_rust//rust:repositories.bzl", "rust_repositories")

rust_repositories()

## add the external repositories for the python toolchain
http_archive(
    name = "rules_python",
    sha256 = "778197e26c5fbeb07ac2a2c5ae405b30f6cb7ad1f5510ea6fdac03bded96cc6f",
    url = "https://github.com/bazelbuild/rules_python/releases/download/0.2.0/rules_python-0.2.0.tar.gz",
)

load("@rules_proto_grpc//python:repositories.bzl", rules_proto_grpc_python_repos = "python_repos")

rules_proto_grpc_python_repos()

grpc_deps()
