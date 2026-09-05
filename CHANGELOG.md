# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.1] - 2026-09-05

### Fixed

- Bech32 decode rejects mixed-case strings (BIP-173).
- Segwit decode rejects invalid HRPs and empty / wrong-length witness programs.

This is a P1 correctness slice, not a certification that every remaining public API is PASS.

[1.0.1]: https://github.com/ImL1s/kotlin-address/releases/tag/v1.0.1
