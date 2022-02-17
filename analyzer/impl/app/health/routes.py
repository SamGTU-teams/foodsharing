from app.health import bp, health
import logging

log = logging.getLogger(__name__)


bp.add_url_rule("/health", "health", view_func=lambda: health.run())
