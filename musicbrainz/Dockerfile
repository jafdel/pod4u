FROM musicbrainz:latest
RUN apt update -y && apt install curl build-essential make gcc -y && apt remove --allow-remove-essential perl perl-modules perl-base -y && apt purge perl perl-modules perl-base -y && apt autoremove -y && apt clean && dpkg --configure -a
RUN apt install git -y && git clone https://github.com/tokuhirom/plenv.git ~/.plenv && echo 'export PATH="/root/.plenv/bin:$PATH"' >> ~/.profile && echo 'eval "$(plenv init -)"' >> ~/.profile && /root/.plenv/bin/plenv init -
SHELL ["/bin/bash", "-c"]
RUN git clone https://github.com/tokuhirom/Perl-build.git /root/.plenv/plugins/perl-build && mkdir -p /root/.plenv/versions && curl -o /perl-5.42.2.tar.gz https://www.cpan.org/src/5.0/perl-5.42.2.tar.gz && tar -xzf /perl-5.42.2.tar.gz --no-same-owner
WORKDIR /perl-5.42.2
RUN ./Configure -de -Dprefix=/root/.plenv/versions -Aeval:scriptdir=/root/.plenv/versions -Dcc=gcc && make prefix=/root/.plenv/versions && make install DESTDIR=/root/.plenv/versions && /root/.plenv/bin/plenv rehash
RUN echo "UID_MAX 197609" >> /etc/login.defs && echo "GID_MAX 197609" >> /etc/login.defs && groupadd -g 197609 tar && useradd -u 197609 -g tar tar && echo "tar:197609:197609" >> /etc/passwd
RUN apt install -f && apt install pkg-config gnupg postgresql-18 postgresql-contrib-18 postgresql-server-dev-18 valkey nodejs postgresql-client-18 file moreutils dos2unix bzip2 -y && corepack enable
WORKDIR /usr/local/bin/
RUN git clone --recursive https://github.com/metabrainz/musicbrainz-server.git
WORKDIR ./musicbrainz-server
COPY ./InitDb.pl ./admin/
RUN echo 'sub REPLICATION_TYPE { RT_STANDALONE }' >> ./lib/DBDefs.pm.sample && cp ./lib/DBDefs.pm.sample ./lib/DBDefs.pm && apt install libdb-dev libexpat1-dev libicu-dev liblocal-lib-perl libpq-dev libxml2 libxml2-dev liblwp-protocol-https-perl -y && dos2unix ./admin/InitDb.pl
RUN echo 'eval $( perl -Mlocal::lib )' >> ~/.bashrc && echo 'PERL_MM_OPT=""; PERL_MB_OPT=""; PERL_MM_USE_DEFAULT=1' >> ~/.bashrc && source ~/.bashrc && apt install libauthen-passphrase-perl libencode-detect-perl -y
WORKDIR /root/.plenv/bin/
RUN chmod 755 /usr/local/include && chmod 755 /usr/local/lib && export PLENV_VERSION=5.42.2 && curl -L https://cpanmin.us | perl - App::cpanminus
RUN cpanm Email::Address::XS Statistics::Basic DBIx::Connector Class::Load Perl::Critic::Policy::Variables::ProhibitUnusedVarsStricter List::AllUtils URI Cache::Null HTML::HTML5::Parser Test::Mock::Class HTML::Tiny Template::Plugin::JavaScript Catalyst::Plugin::Static::Simple Plack MooseX::MethodAttributes String::CamelCase Throwable Moose Data::Page Digest::MD5 TAP::Harness::JUnit Test::Fatal Catalyst::Plugin::Cache XML::XPath XML::RSS::Parser::Lite DateTime::Format::Pg TAP::Parser::SourceHandler::pgTAP LWP::UserAgent::Mockable MooseX::Runnable Template::Toolkit Digest::HMAC_SHA1 HTML::TreeBuilder::XPath Text::Trim Test::JSON MooseX::Types::URI Test::LongString DateTime::TimeZone String::TT Digest::MD5::File Test::XPath Clone Email::MIME DateTime::Format::ISO8601 DateTime::Format::Duration CGI::Expand DBD::Pg Catalyst::Action::RenderView Catalyst::Plugin::Cache::HTTP DateTime::Locale Data::UUID::MT HTML::FormHandler LWP::Protocol::https Data::Dumper::Concise List::UtilsBy Test::WWW::Mechanize::Catalyst MooseX::Clone MooseX::Types::Structured MooseX::Types Email::Sender Template::Plugin::Class MooseX::Getopt Catalyst::View::TT HTML::Selector::XPath Set::Scalar Test::Memory::Cycle Date::Calc Text::Diff3 JSON Test::Differences Module::Pluggable DateTime::Format::Natural MooseX::Singleton Redis Text::Markdown Perl::Critic::Pulp Catalyst::Plugin::Authentication Template::Plugin::JSON::Escape Email::Valid Method::Signatures::Simple MooseX::Role::Parameterized Unicode::ICU::Collator DBI REST::Utils Catalyst::Runtime Perl::Critic Data::Compare Perl::Critic::Moose Math::Random::Secure Text::Unaccent::PurePerl Perl::Critic::TooMuchCode Test::Aggregate XML::LibXML Algorithm::Diff Exception::Class CGI::Simple Catalyst::Plugin::Session::State::Cookie XML::Parser XML::SemanticDiff Log::Dispatch Catalyst::Authentication::Credential::HTTP XML::Simple Locale::Messages XML::Parser::Lite Plack::Middleware::Debug Text::WikiFormat Test::Routine Hash::Merge String::ShellQuote --force
WORKDIR ./
ENV POSTGRES_USER="musicbrainz"
ENV POSTGRES_PASSWORD="musicbrainz"
ENV POSTGRES_DB="musicbrainz_db"
ENV POSTGRES_HOST="localhost"
ENV POSTGRES_ADDRESS="musicbrainz"
ENV POSTGRES_PORT="5432"
EXPOSE 5432
WORKDIR /docker-entrypoint-initdb.d/
COPY ./restore.sh /docker-entrypoint-initdb.d/
COPY ./pg_hba.conf /var/lib/postgresql/18/
COPY ./pg_ident.conf /var/lib/postgresql/18/
RUN usermod -aG sudo postgres
USER root
RUN dos2unix /docker-entrypoint-initdb.d/restore.sh && chown postgres:postgres /docker-entrypoint-initdb.d/restore.sh && chmod +x /docker-entrypoint-initdb.d/restore.sh