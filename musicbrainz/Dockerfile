FROM musicbrainz:latest
#COPY ./resolv.conf /etc
COPY ./pg_hba.conf /var/lib/postgresql/18/
COPY ./pg_ident.conf /var/lib/postgresql/18/
#COPY ./mbdump.tar.bz2 /
RUN apt clean && apt install -f && apt update && apt upgrade -y && apt remove --allow-remove-essential perl perl-modules perl-base -y && apt purge perl perl-modules perl-base -y && apt autoremove -y && dpkg --configure -a && apt install curl build-essential make gcc git -y
RUN git clone https://github.com/tokuhirom/plenv.git ~/.plenv && echo 'export PATH="/root/.plenv/bin:$PATH"' >> ~/.profile && echo 'eval "$(plenv init -)"' >> ~/.profile && /root/.plenv/bin/plenv init -
RUN git clone --recursive https://github.com/metabrainz/musicbrainz-server.git /musicbrainz-server
#COPY ./InitDb.pl /musicbrainz-server/admin/
SHELL ["/bin/bash", "-c"]
COPY ./perl-5.42.2.tar.gz /
RUN echo "UID_MAX 197609" >> /etc/login.defs && echo "GID_MAX 197609" >> /etc/login.defs && groupadd -g 197609 tar && useradd -u 197609 -g tar tar && echo "tar:197609:197609" >> /etc/passwd
RUN git clone https://github.com/tokuhirom/Perl-build.git /root/.plenv/plugins/perl-build && mkdir -p /root/.plenv/versions
RUN apt install -f && apt install pkg-config postgresql-18 postgresql-contrib-18 postgresql-server-dev-18 valkey nodejs postgresql-client-18 file moreutils dos2unix bzip2 -y && corepack enable
WORKDIR /usr/local/bin/
RUN echo 'sub REPLICATION_TYPE { RT_STANDALONE }' >> /musicbrainz-server/lib/DBDefs.pm.sample && cp /musicbrainz-server/lib/DBDefs.pm.sample /musicbrainz-server/lib/DBDefs.pm && apt install libdb-dev libexpat1-dev libicu-dev liblocal-lib-perl libpq-dev libxml2 libxml2-dev liblwp-protocol-https-perl -y && dos2unix /musicbrainz-server/admin/InitDb.pl
RUN echo 'eval $( perl -Mlocal::lib )' >> ~/.bashrc && echo 'PERL_MM_OPT=""; PERL_MB_OPT=""; PERL_MM_USE_DEFAULT=1' >> ~/.bashrc && source ~/.bashrc && apt install libauthen-passphrase-perl libencode-detect-perl -y
WORKDIR /root/.plenv/versions
RUN chmod 755 /usr/local/include && chmod 755 /usr/local/lib && export PLENV_VERSION=5.42.2 && apt install -y cpanminus && cpanm Perl::Build && perl-build /perl-5.42.2.tar.gz /root/.plenv/versions/perl-5.42.2
RUN /root/.plenv/bin/plenv install 5.42.2 -Dusethreads --tarball-dir / && /root/.plenv/bin/plenv global 5.42.2 && /root/.plenv/bin/plenv rehash
RUN apt install -y libpod-readme-perl libc-bin && /root/.plenv/bin/plenv install-cpanm && curl https://cpan.metacpan.org/authors/id/C/CH/CHRISN/Net-SSLeay-1.96.tar.gz -o ./Net-SSLeay-1.96.tar.gz && tar -xzf ./Net-SSLeay-1.96.tar.gz --no-same-owner
WORKDIR Net-SSLeay-1.96
RUN apt install -y libssl-dev zlib1g-dev && perl Makefile.PL && make && make install && /root/.plenv/bin/plenv exec cpanm IO::Socket::SSL::Utils --force && /root/.plenv/bin/plenv exec cpanm LWP::Protocol::https
WORKDIR /musicbrainz-server
RUN /root/.plenv/bin/plenv exec cpanm --installdeps --notest . 
RUN /root/.plenv/bin/plenv exec cpanm aliased #Moose DateTime::Duration List::AllUtils Throwable::Error DBIx::Connector DBD::Pg
ENV POSTGRES_USER="musicbrainz"
ENV POSTGRES_PASSWORD="musicbrainz"
ENV POSTGRES_DB="musicbrainz_db"
ENV POSTGRES_HOST="localhost"
ENV POSTGRES_ADDRESS="musicbrainz"
ENV POSTGRES_PORT="5432"
EXPOSE 5432
RUN dos2unix /docker-entrypoint-initdb.d/pg.sh && chown postgres:postgres /docker-entrypoint-initdb.d/pg.sh && chmod +x /docker-entrypoint-initdb.d/pg.sh
#RUN cpanm Email::Address::XS Statistics::Basic DBIx::Connector Class::Load Perl::Critic::Policy::Variables::ProhibitUnusedVarsStricter List::AllUtils URI Cache::Null HTML::HTML5::Parser Test::Mock::Class HTML::Tiny Template::Plugin::JavaScript Catalyst::Plugin::Static::Simple Plack MooseX::MethodAttributes String::CamelCase Throwable Moose Data::Page Digest::MD5 TAP::Harness::JUnit Test::Fatal Catalyst::Plugin::Cache XML::XPath XML::RSS::Parser::Lite DateTime::Format::Pg TAP::Parser::SourceHandler::pgTAP LWP::UserAgent::Mockable MooseX::Runnable Template::Toolkit Digest::HMAC_SHA1 HTML::TreeBuilder::XPath Text::Trim Test::JSON MooseX::Types::URI Test::LongString DateTime::TimeZone String::TT Digest::MD5::File Test::XPath Clone Email::MIME DateTime::Format::ISO8601 DateTime::Format::Duration CGI::Expand DBD::Pg Catalyst::Action::RenderView Catalyst::Plugin::Cache::HTTP DateTime::Locale Data::UUID::MT HTML::FormHandler LWP::Protocol::https Data::Dumper::Concise List::UtilsBy Test::WWW::Mechanize::Catalyst MooseX::Clone MooseX::Types::Structured MooseX::Types Email::Sender Template::Plugin::Class MooseX::Getopt Catalyst::View::TT HTML::Selector::XPath Set::Scalar Test::Memory::Cycle Date::Calc Text::Diff3 JSON Test::Differences Module::Pluggable DateTime::Format::Natural MooseX::Singleton Redis Text::Markdown Perl::Critic::Pulp Catalyst::Plugin::Authentication Template::Plugin::JSON::Escape Email::Valid Method::Signatures::Simple MooseX::Role::Parameterized Unicode::ICU::Collator DBI REST::Utils Catalyst::Runtime Perl::Critic Data::Compare Perl::Critic::Moose Math::Random::Secure Text::Unaccent::PurePerl Perl::Critic::TooMuchCode Test::Aggregate XML::LibXML Algorithm::Diff Exception::Class CGI::Simple Catalyst::Plugin::Session::State::Cookie XML::Parser XML::SemanticDiff Log::Dispatch Catalyst::Authentication::Credential::HTTP XML::Simple Locale::Messages XML::Parser::Lite Plack::Middleware::Debug Text::WikiFormat Test::Routine Hash::Merge String::ShellQuote --force